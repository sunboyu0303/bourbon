package com.github.bourbon.base.constant;

import java.time.format.DateTimeFormatter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/24 19:54
 */
public final class DateConstants {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static final DateTimeFormatter DEFAULT_LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter DEFAULT_LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static final DateTimeFormatter DEFAULT_LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateConstants() {
    }
}