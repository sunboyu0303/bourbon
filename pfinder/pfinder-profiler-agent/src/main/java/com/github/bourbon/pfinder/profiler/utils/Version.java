package com.github.bourbon.pfinder.profiler.utils;

import com.github.bourbon.base.utils.CharSequenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 10:33
 */
public class Version {
    private final Pattern pattern = Pattern.compile("(\\d+)");
    private final List<Integer> parts = new ArrayList<>();

    public static Version parse(String versionStr) {
        return new Version(versionStr);
    }

    private Version(String versionStr) {
        if (CharSequenceUtils.isBlank(versionStr)) {
            throw new IllegalArgumentException("version must not blank");
        }
        Matcher matcher = this.pattern.matcher(versionStr);
        while (matcher.find()) {
            this.parts.add(Integer.parseInt(matcher.group(0)));
        }
    }

    private int compareTo(String version) {
        if (CharSequenceUtils.isBlank(version)) {
            throw new IllegalArgumentException("compare version must not blank");
        }
        Matcher matcher = this.pattern.matcher(version);
        int i = 0;
        int compareResult;
        do {
            if (!matcher.find()) {
                if (i < this.parts.size()) {
                    return 1;
                }
                return 0;
            }
            if (i >= this.parts.size()) {
                return -1;
            }
            int subVersion = Integer.parseInt(matcher.group(0));
            compareResult = this.parts.get(i++).compareTo(subVersion);
        } while (compareResult == 0);
        return compareResult;
    }

    public boolean greaterThan(String version) {
        return this.compareTo(version) > 0;
    }

    public boolean greaterOrEqualsThan(String version) {
        return this.compareTo(version) >= 0;
    }

    public boolean lessThan(String version) {
        return this.compareTo(version) < 0;
    }

    public boolean lessOrEqualsThan(String version) {
        return this.compareTo(version) <= 0;
    }

    public boolean equalsThan(String version) {
        return this.compareTo(version) == 0;
    }

    public boolean inRange(String versionRange) {
        RangeExpression range = RangeExpression.parse(versionRange);
        String lowLimit = range.getLowLimit();
        if (CharSequenceUtils.isNotEmpty(lowLimit)) {
            boolean lowTest = range.isIncludeLowBound() ? this.greaterOrEqualsThan(lowLimit) : this.greaterThan(lowLimit);
            if (!lowTest) {
                return false;
            }
        }
        String highLimit = range.getHighLimit();
        if (CharSequenceUtils.isNotEmpty(highLimit)) {
            return range.isIncludeHighBound() ? this.lessOrEqualsThan(highLimit) : this.lessThan(highLimit);
        }
        return true;
    }
}