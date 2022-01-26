package com.github.bourbon.base.appender.config;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/26 10:37
 */
public class LogReserveConfig {

    private final int day;

    private final int hour;

    public LogReserveConfig(int day, int hour) {
        this.day = day;
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }
}