package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.time.Duration;
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
 * @date 2021/12/26 09:32
 */
public enum DurationStyle {

    SIMPLE("^([+-]?\\d+)([a-zA-Z]{0,2})$") {
        @Override
        public Duration parse(String value, ChronoUnit unit) {
            try {
                Matcher matcher = matcher(value);
                Assert.isTrue(matcher.matches(), "Does not match simple duration pattern");
                String suffix = matcher.group(2);
                return BooleanUtils.defaultSupplierIfFalse(CharSequenceUtils.isEmpty(suffix), () -> Unit.fromChronoUnit(unit), () -> Unit.fromSuffix(suffix)).parse(matcher.group(1));
            } catch (Exception ex) {
                throw new IllegalArgumentException("'" + value + "' is not a valid simple duration", ex);
            }
        }

        @Override
        public String print(Duration value, ChronoUnit unit) {
            return Unit.fromChronoUnit(unit).print(value);
        }
    },
    ISO8601("^[+-]?P.*$") {
        @Override
        public Duration parse(String value, ChronoUnit unit) {
            try {
                return Duration.parse(value);
            } catch (Exception ex) {
                throw new IllegalArgumentException("'" + value + "' is not a valid ISO-8601 duration", ex);
            }
        }

        @Override
        public String print(Duration value, ChronoUnit unit) {
            return value.toString();
        }
    };

    private final Pattern pattern;

    DurationStyle(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    protected final boolean matches(String value) {
        return pattern.matcher(value).matches();
    }

    protected final Matcher matcher(String value) {
        return pattern.matcher(value);
    }

    public Duration parse(String value) {
        return parse(value, null);
    }

    public abstract Duration parse(String value, ChronoUnit unit);

    public String print(Duration value) {
        return print(value, null);
    }

    public abstract String print(Duration value, ChronoUnit unit);

    public static Duration detectAndParse(String value) {
        return detectAndParse(value, null);
    }

    public static Duration detectAndParse(String value, ChronoUnit unit) {
        return detect(value).parse(value, unit);
    }

    public static DurationStyle detect(String value) {
        Assert.notNull(value, "Value must not be null");
        for (DurationStyle candidate : values()) {
            if (candidate.matches(value)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("'" + value + "' is not a valid duration");
    }

    enum Unit {
        NANOS(ChronoUnit.NANOS, "ns", Duration::toNanos),
        MICROS(ChronoUnit.MICROS, "us", duration -> duration.toNanos() / 1000L),
        MILLIS(ChronoUnit.MILLIS, "ms", Duration::toMillis),
        SECONDS(ChronoUnit.SECONDS, "s", Duration::getSeconds),
        MINUTES(ChronoUnit.MINUTES, "m", Duration::toMinutes),
        HOURS(ChronoUnit.HOURS, "h", Duration::toHours),
        DAYS(ChronoUnit.DAYS, "d", Duration::toDays);

        private static final Map<ChronoUnit, Unit> unitMap;
        private static final Map<String, Unit> suffixMap;

        static {
            unitMap = Arrays.stream(values()).collect(Collectors.toMap(Unit::getChronoUnit, Function.identity()));
            suffixMap = Arrays.stream(values()).collect(Collectors.toMap(unit -> unit.suffix.toLowerCase(), Function.identity()));
        }

        private final ChronoUnit chronoUnit;
        private final String suffix;
        private Function<Duration, Long> longValue;

        Unit(ChronoUnit chronoUnit, String suffix, Function<Duration, Long> toUnit) {
            this.chronoUnit = chronoUnit;
            this.suffix = suffix;
            this.longValue = toUnit;
        }

        private ChronoUnit getChronoUnit() {
            return chronoUnit;
        }

        public Duration parse(String value) {
            return Duration.of(Long.parseLong(value), chronoUnit);
        }

        public String print(Duration value) {
            return longValue(value) + suffix;
        }

        public long longValue(Duration value) {
            return longValue.apply(value);
        }

        public static Unit fromChronoUnit(ChronoUnit chronoUnit) {
            return ObjectUtils.defaultIfNullElseFunction(chronoUnit, unit -> ObjectUtils.requireNonNull(unitMap.get(unit), () -> new IllegalArgumentException("Unknown unit " + chronoUnit)), Unit.MILLIS);
        }

        public static Unit fromSuffix(String suffix) {
            return ObjectUtils.defaultIfNullElseFunction(suffix, s -> ObjectUtils.requireNonNull(suffixMap.get(s.toLowerCase()), () -> new IllegalArgumentException("Unknown unit '" + suffix + "'")), Unit.MILLIS);
        }
    }
}