package com.github.bourbon.pfinder.profiler.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 10:44
 */
public class RangeExpression {
    private static final Pattern EXP_PATTERN = Pattern.compile("^\\s*([\\[(])\\s*([^,\\s]+)?\\s*,\\s*([^,\\s]+)?\\s*([)\\]])\\s*$");
    private final boolean includeLowBound;
    private final boolean includeHighBound;
    private final String lowLimit;
    private final String highLimit;

    public static RangeExpression parse(String exp) {
        Matcher matcher = EXP_PATTERN.matcher(exp);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("invalid RangeThreshold expression. exp=" + exp);
        }
        return new RangeExpression(matcher.group(1).equals(StringConstants.LEFT_BRACKETS), matcher.group(4).equals(StringConstants.RIGHT_BRACKETS), matcher.group(2), matcher.group(3));
    }

    private RangeExpression(boolean includeLowBound, boolean includeHighBound, String lowLimit, String highLimit) {
        this.includeLowBound = includeLowBound;
        this.includeHighBound = includeHighBound;
        this.lowLimit = ObjectUtils.defaultIfNullElseFunction(lowLimit, String::trim, StringConstants.EMPTY);
        this.highLimit = ObjectUtils.defaultIfNullElseFunction(highLimit, String::trim, StringConstants.EMPTY);
    }

    public boolean isIncludeLowBound() {
        return this.includeLowBound;
    }

    public boolean isIncludeHighBound() {
        return this.includeHighBound;
    }

    public String getLowLimit() {
        return this.lowLimit;
    }

    public String getHighLimit() {
        return this.highLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            RangeExpression that = (RangeExpression) o;
            if (this.includeLowBound != that.includeLowBound) {
                return false;
            }
            if (this.includeHighBound != that.includeHighBound) {
                return false;
            }
            if (this.lowLimit != null) {
                if (this.lowLimit.equals(that.lowLimit)) {
                    return ObjectUtils.equals(this.highLimit, that.highLimit);
                }
            } else if (that.lowLimit == null) {
                return ObjectUtils.equals(this.highLimit, that.highLimit);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = this.includeLowBound ? 1 : 0;
        result = 31 * result + (this.includeHighBound ? 1 : 0);
        result = 31 * result + (this.lowLimit != null ? this.lowLimit.hashCode() : 0);
        result = 31 * result + (this.highLimit != null ? this.highLimit.hashCode() : 0);
        return result;
    }
}