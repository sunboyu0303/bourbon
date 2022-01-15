package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.DateConstants;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/29 14:39
 */
public interface LocalDateTimeUtils {

    static LocalDate localDate(int y, int m, int d) {
        return LocalDate.of(y, m, d);
    }

    static String localDateFormat(LocalDate localDate) {
        return localDateFormat(localDate, DateConstants.LOCAL_DATE_FORMATTER);
    }

    static String localDateFormat(LocalDate localDate, DateTimeFormatter f) {
        return localDate.format(f);
    }

    static LocalDate localDateNow() {
        return LocalDate.now();
    }

    static String localDateNowFormat() {
        return localDateNowFormat(DateConstants.LOCAL_DATE_FORMATTER);
    }

    static String localDateNowFormat(DateTimeFormatter f) {
        return localDateNow().format(f);
    }

    static LocalTime localTime(int h, int m) {
        return LocalTime.of(h, m);
    }

    static LocalTime localTime(int h, int m, int s) {
        return LocalTime.of(h, m, s);
    }

    static LocalTime localTime(int h, int m, int s, int n) {
        return LocalTime.of(h, m, s, n);
    }

    static String localTimeFormat(LocalTime localTime) {
        return localTimeFormat(localTime, DateConstants.FORMATTER);
    }

    static String localTimeFormat(LocalTime localTime, DateTimeFormatter f) {
        return localTime.format(f);
    }

    static LocalTime localTimeNow() {
        return LocalTime.now();
    }

    static String localTimeNowFormat() {
        return localTimeNowFormat(DateConstants.FORMATTER);
    }

    static String localTimeNowFormat(DateTimeFormatter f) {
        return localTimeNow().format(f);
    }

    static LocalDateTime localDateTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time);
    }

    static LocalDateTime localDateTime(int y, int m, int d, int h, int min) {
        return localDateTime(localDate(y, m, d), localTime(h, min));
    }

    static LocalDateTime localDateTime(int y, int m, int d, int h, int min, int s) {
        return localDateTime(localDate(y, m, d), localTime(h, min, s));
    }

    static LocalDateTime localDateTime(int y, int m, int d, int h, int min, int s, int n) {
        return localDateTime(localDate(y, m, d), localTime(h, min, s, n));
    }

    static String localDateTimeFormat(LocalDateTime localDateTime) {
        return localDateTimeFormat(localDateTime, DateConstants.FORMATTER);
    }

    static String localDateTimeFormat(LocalDateTime localDateTime, DateTimeFormatter f) {
        return localDateTime.format(f);
    }

    static LocalDateTime localDateTimeNow() {
        return LocalDateTime.now();
    }

    static String localDateTimeNowFormat() {
        return localDateTimeNowFormat(DateConstants.FORMATTER);
    }

    static String localDateTimeNowFormat(DateTimeFormatter f) {
        return localDateTimeNow().format(f);
    }

    static LocalDateTime localDateTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    static String localDateTimeFormat(long time) {
        return localDateTimeFormat(time, DateConstants.FORMATTER);
    }

    static String localDateTimeFormat(long time, DateTimeFormatter f) {
        return localDateTime(time).format(f);
    }

    static long toMillis(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    static long toSeconds(LocalDateTime time) {
        return TimeUnit.MILLISECONDS.toSeconds(toMillis(time));
    }

    static long toMinutes(LocalDateTime time) {
        return TimeUnit.MILLISECONDS.toMinutes(toMillis(time));
    }

    static long toHours(LocalDateTime time) {
        return TimeUnit.MILLISECONDS.toHours(toMillis(time));
    }

    static long toDays(LocalDateTime time) {
        return TimeUnit.MILLISECONDS.toDays(toMillis(time));
    }
}