package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.DateConstants;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/5 14:25
 */
public final class Timestamp {

    public static String currentTime() {
        return LocalDateTimeUtils.localDateTimeNowFormat(DateConstants.DEFAULT_FORMATTER);
    }

    public static String format(long time) {
        return LocalDateTimeUtils.localDateTimeFormat(time, DateConstants.DEFAULT_FORMATTER);
    }

    private Timestamp() {
    }
}