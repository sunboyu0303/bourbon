package com.github.bourbon.pfinder.profiler.sdk.util;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.ArrayUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 21:23
 */
public final class MetricNameUtil {

    public static String name(Class<?> clz) {
        return clz.getName();
    }

    public static String name(Class<?> clz, String... strings) {
        StringBuilder sb = new StringBuilder(clz.getName());
        if (ArrayUtils.isNotEmpty(strings)) {
            for (String string : strings) {
                sb.append(StringConstants.DOT).append(string);
            }
        }
        return sb.toString();
    }

    public static String name(String... strings) {
        return ArrayUtils.isEmpty(strings) ? StringConstants.EMPTY : String.join(StringConstants.DOT, strings);
    }

    private MetricNameUtil() {
        throw new UnsupportedOperationException();
    }
}