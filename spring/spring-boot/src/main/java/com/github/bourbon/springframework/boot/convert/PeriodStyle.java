package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 11:44
 */
public enum PeriodStyle {

    SIMPLE("^" + "(?:([-+]?[0-9]+)Y)?" + "(?:([-+]?[0-9]+)M)?" + "(?:([-+]?[0-9]+)W)?" + "(?:([-+]?[0-9]+)D)?" + "$", Pattern.CASE_INSENSITIVE) {
        @Override
        public Period parse(String value, ChronoUnit unit) {
            try {
                return BooleanUtils.defaultSupplierIfFalse(NUMERIC.matcher(value).matches(), () -> Unit.fromChronoUnit(unit).parse(value), () -> {
                    Matcher matcher = matcher(value);
                    Assert.isTrue(matcher.matches(), "Does not match simple period pattern");
                    Assert.isTrue(hasAtLeastOneGroupValue(matcher), () -> "'" + value + "' is not a valid simple period");
                    int years = parseInt(matcher, 1);
                    int months = parseInt(matcher, 2);
                    int weeks = parseInt(matcher, 3);
                    int days = parseInt(matcher, 4);
                    return Period.of(years, months, Math.addExact(Math.multiplyExact(weeks, 7), days));
                });
            } catch (Exception ex) {
                throw new IllegalArgumentException("'" + value + "' is not a valid simple period", ex);
            }
        }

        boolean hasAtLeastOneGroupValue(Matcher matcher) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                if (matcher.group(i + 1) != null) {
                    return true;
                }
            }
            return false;
        }

        private int parseInt(Matcher matcher, int group) {
            return ObjectUtils.defaultIfNull(matcher.group(group), Integer::parseInt, 0);
        }

        @Override
        protected boolean matches(String value) {
            return NUMERIC.matcher(value).matches() || matcher(value).matches();
        }

        @Override
        public String print(Period value, ChronoUnit unit) {
            return BooleanUtils.defaultSupplierIfFalse(value.isZero(), () -> Unit.fromChronoUnit(unit).print(value), () -> {
                StringBuilder result = new StringBuilder();
                append(result, value, Unit.YEARS);
                append(result, value, Unit.MONTHS);
                append(result, value, Unit.DAYS);
                return result.toString();
            });
        }

        private void append(StringBuilder result, Period value, Unit unit) {
            if (!unit.isZero(value)) {
                result.append(unit.print(value));
            }
        }
    },

    ISO8601("^[+-]?P.*$", 0) {
        @Override
        public Period parse(String value, ChronoUnit unit) {
            try {
                return Period.parse(value);
            } catch (Exception ex) {
                throw new IllegalArgumentException("'" + value + "' is not a valid ISO-8601 period", ex);
            }
        }

        @Override
        public String print(Period value, ChronoUnit unit) {
            return value.toString();
        }
    };

    private static final Pattern NUMERIC = Pattern.compile("^[-+]?[0-9]+$");

    private final Pattern pattern;

    PeriodStyle(String pattern, int flags) {
        this.pattern = Pattern.compile(pattern, flags);
    }

    protected boolean matches(String value) {
        return pattern.matcher(value).matches();
    }

    protected final Matcher matcher(String value) {
        return pattern.matcher(value);
    }

    public Period parse(String value) {
        return parse(value, null);
    }

    public abstract Period parse(String value, ChronoUnit unit);

    public String print(Period value) {
        return print(value, null);
    }

    public abstract String print(Period value, ChronoUnit unit);

    public static Period detectAndParse(String value) {
        return detectAndParse(value, null);
    }

    public static Period detectAndParse(String value, ChronoUnit unit) {
        return detect(value).parse(value, unit);
    }

    public static PeriodStyle detect(String value) {
        Assert.notNull(value, "Value must not be null");
        for (PeriodStyle candidate : values()) {
            if (candidate.matches(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid period");
    }

    private enum Unit {
        DAYS(ChronoUnit.DAYS, "d", Period::getDays, Period::ofDays),
        WEEKS(ChronoUnit.WEEKS, "w", null, Period::ofWeeks),
        MONTHS(ChronoUnit.MONTHS, "m", Period::getMonths, Period::ofMonths),
        YEARS(ChronoUnit.YEARS, "y", Period::getYears, Period::ofYears);

        private static final Map<ChronoUnit, Unit> unitMap = Arrays.stream(values()).collect(Collectors.toMap(Unit::getChronoUnit, Function.identity()));

        private final ChronoUnit chronoUnit;
        private final String suffix;
        private final Function<Period, Integer> intValue;
        private final Function<Integer, Period> factory;

        Unit(ChronoUnit chronoUnit, String suffix, Function<Period, Integer> intValue, Function<Integer, Period> factory) {
            this.chronoUnit = chronoUnit;
            this.suffix = suffix;
            this.intValue = intValue;
            this.factory = factory;
        }

        private ChronoUnit getChronoUnit() {
            return chronoUnit;
        }

        private Period parse(String value) {
            return factory.apply(Integer.parseInt(value));
        }

        private String print(Period value) {
            return intValue(value) + suffix;
        }

        private boolean isZero(Period value) {
            return intValue(value) == 0;
        }

        private int intValue(Period value) {
            Assert.notNull(intValue, () -> "intValue cannot be extracted from " + name());
            return intValue.apply(value);
        }

        private static Unit fromChronoUnit(ChronoUnit chronoUnit) {
            return ObjectUtils.defaultIfNull(chronoUnit, unit -> ObjectUtils.requireNonNull(unitMap.get(unit), () -> new IllegalArgumentException("Unsupported unit " + chronoUnit)), Unit.DAYS);
        }
    }
}