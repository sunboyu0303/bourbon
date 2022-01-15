package com.github.bourbon.springframework.core.env;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/30 16:49
 */
class RelaxedNames implements Iterable<String> {

    private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([^A-Z-])([A-Z])");
    private static final Pattern SEPARATED_TO_CAMEL_CASE_PATTERN = Pattern.compile("[_\\-.]");

    private final Set<String> values = SetUtils.newLinkedHashSet();

    RelaxedNames(String name) {
        initialize(name, values);
    }

    private void initialize(String name, Set<String> values) {
        if (!values.contains(name)) {
            for (Variation variation : Variation.values()) {
                for (Manipulation manipulation : Manipulation.values()) {
                    String result = variation.apply(manipulation.apply(name));
                    values.add(result);
                    initialize(result, values);
                }
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    enum Manipulation {
        NONE {
            @Override
            String apply(String value) {
                return value;
            }
        },
        HYPHEN_TO_UNDERSCORE {
            @Override
            String apply(String value) {
                return BooleanUtils.defaultIfPredicate(value, v -> v.indexOf(CharConstants.HYPHEN) != -1, v -> v.replace(CharConstants.HYPHEN, CharConstants.UNDERLINE), value);
            }
        },
        UNDERSCORE_TO_PERIOD {
            @Override
            String apply(String value) {
                return BooleanUtils.defaultIfPredicate(value, v -> v.indexOf(CharConstants.UNDERLINE) != -1, v -> v.replace(CharConstants.UNDERLINE, CharConstants.DOT), value);
            }
        },
        PERIOD_TO_UNDERSCORE {
            @Override
            String apply(String value) {
                return BooleanUtils.defaultIfPredicate(value, v -> v.indexOf(CharConstants.DOT) != -1, v -> v.replace(CharConstants.DOT, CharConstants.UNDERLINE), value);
            }
        },
        CAMELCASE_TO_UNDERSCORE {
            @Override
            String apply(String value) {
                if (value.isEmpty()) {
                    return value;
                }
                Matcher matcher = CAMEL_CASE_PATTERN.matcher(value);
                if (!matcher.find()) {
                    return value;
                }
                matcher.reset();
                StringBuffer result = new StringBuffer();
                while (matcher.find()) {
                    matcher.appendReplacement(result, matcher.group(1) + CharConstants.UNDERLINE + StringUtils.uncapitalize(matcher.group(2)));
                }
                matcher.appendTail(result);
                return result.toString();
            }
        },
        CAMELCASE_TO_HYPHEN {
            @Override
            String apply(String value) {
                if (value.isEmpty()) {
                    return value;
                }
                Matcher matcher = CAMEL_CASE_PATTERN.matcher(value);
                if (!matcher.find()) {
                    return value;
                }
                matcher.reset();
                StringBuffer result = new StringBuffer();
                while (matcher.find()) {
                    matcher.appendReplacement(result, matcher.group(1) + CharConstants.HYPHEN + StringUtils.uncapitalize(matcher.group(2)));
                }
                matcher.appendTail(result);
                return result.toString();
            }
        },
        SEPARATED_TO_CAMELCASE {
            @Override
            String apply(String value) {
                return separatedToCamelCase(value, false);
            }
        },
        CASE_INSENSITIVE_SEPARATED_TO_CAMELCASE {
            @Override
            String apply(String value) {
                return separatedToCamelCase(value, true);
            }
        };

        abstract String apply(String value);

        private static final char[] SUFFIXES = {CharConstants.UNDERLINE, CharConstants.HYPHEN, CharConstants.DOT};

        private static String separatedToCamelCase(String value, boolean caseInsensitive) {
            return BooleanUtils.defaultSupplierIfPredicate(value, String::isEmpty, Function.identity(), () -> {
                StringBuilder builder = new StringBuilder();
                for (String field : SEPARATED_TO_CAMEL_CASE_PATTERN.split(value)) {
                    builder.append(BooleanUtils.defaultSupplierIfFalse(
                            builder.length() == 0, () -> BooleanUtils.defaultIfFalse(caseInsensitive, field::toLowerCase, field), () -> StringUtils.capitalize(field)
                    ));
                }
                char lastChar = value.charAt(value.length() - 1);
                for (char suffix : SUFFIXES) {
                    if (lastChar == suffix) {
                        builder.append(suffix);
                        break;
                    }
                }
                return builder.toString();
            });
        }
    }

    enum Variation {
        NONE {
            @Override
            String apply(String value) {
                return value;
            }
        },
        LOWERCASE {
            @Override
            String apply(String value) {
                return BooleanUtils.defaultSupplierIfPredicate(value, String::isEmpty, Function.identity(), value::toLowerCase);
            }
        },
        UPPERCASE {
            @Override
            String apply(String value) {
                return BooleanUtils.defaultSupplierIfPredicate(value, String::isEmpty, Function.identity(), value::toUpperCase);
            }
        };

        abstract String apply(String value);
    }
}