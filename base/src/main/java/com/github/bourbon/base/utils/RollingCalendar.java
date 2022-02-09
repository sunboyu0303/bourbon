package com.github.bourbon.base.utils;

import java.util.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/26 10:42
 */
public class RollingCalendar extends GregorianCalendar {

    private static final long serialVersionUID = -3560331770601814177L;

    private RollingCalendarType type = RollingCalendarType.TROUBLE;

    public RollingCalendar() {
    }

    public RollingCalendar(TimeZone tz, Locale locale) {
        super(tz, locale);
    }

    public RollingCalendar setType(RollingCalendarType type) {
        this.type = type;
        return this;
    }

    public long getNextCheckMillis(Date now) {
        return getNextCheckDate(now).getTime();
    }

    private Date getNextCheckDate(Date now) {
        this.setTime(now);
        switch (type) {
            case SECOND:
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.SECOND, 1);
                break;
            case MINUTE:
                this.set(Calendar.MILLISECOND, 0);
                this.set(Calendar.SECOND, 0);
                this.add(Calendar.MINUTE, 1);
                break;
            case HOUR_OF_DAY:
                this.set(Calendar.MILLISECOND, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MINUTE, 0);
                this.add(Calendar.HOUR_OF_DAY, 1);
                break;
            case DAY_OF_MONTH:
                this.set(Calendar.MILLISECOND, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case WEEK_OF_YEAR:
                this.set(Calendar.MILLISECOND, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
                this.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case MONTH:
                this.set(Calendar.MILLISECOND, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.DAY_OF_MONTH, 1);
                this.add(Calendar.MONTH, 1);
                break;
            default:
                throw new IllegalStateException("Unknown periodicity type.");
        }
        return getTime();
    }

    public enum RollingCalendarType {
        TROUBLE, SECOND, MINUTE, HOUR_OF_DAY, DAY_OF_MONTH, WEEK_OF_YEAR, MONTH
    }
}