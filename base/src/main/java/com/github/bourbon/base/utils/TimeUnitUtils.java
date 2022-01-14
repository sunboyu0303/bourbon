package com.github.bourbon.base.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 14:48
 */
public final class TimeUnitUtils {

    private static final Map<String, TimeUnit> MAP = Arrays.stream(TimeUnit.values()).collect(Collectors.toMap(TimeUnit::name, Function.identity()));

    public static TimeUnit getTimeUnit(String name) {
        return getTimeUnit(name, TimeUnit.MINUTES);
    }

    public static TimeUnit getTimeUnit(String name, TimeUnit defaultTimeUnit) {
        return BooleanUtils.defaultIfPredicate(name, n -> !CharSequenceUtils.isEmpty(n), n -> ObjectUtils.defaultIfNull(MAP.get(n), defaultTimeUnit), defaultTimeUnit);
    }

    public static void sleepMinutes(long time) {
        try {
            TimeUnit.MINUTES.sleep(time);
        } catch (Exception e) {
            // ignore
        }
    }

    public static void sleepSeconds(long time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (Exception e) {
            // ignore
        }
    }

    public static void sleepMilliSeconds(long time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (Exception e) {
            // ignore
        }
    }

    private TimeUnitUtils() {
    }
}