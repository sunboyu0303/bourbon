package com.github.bourbon.base.utils;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/10 09:09
 */
public class RollingCalendarTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        long time = Clock.currentTimeMillis();
        logger.info(Timestamp.format(time));

        Date date = new Date(time);
        RollingCalendar rollingCalendar = new RollingCalendar();
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.SECOND).getNextCheckMillis(date)));
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.MINUTE).getNextCheckMillis(date)));
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.HOUR_OF_DAY).getNextCheckMillis(date)));
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.DAY_OF_MONTH).getNextCheckMillis(date)));
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.WEEK_OF_YEAR).getNextCheckMillis(date)));
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.MONTH).getNextCheckMillis(date)));

        rollingCalendar = new RollingCalendar(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.SECOND).getNextCheckMillis(date)));
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.MINUTE).getNextCheckMillis(date)));
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.HOUR_OF_DAY).getNextCheckMillis(date)));
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.DAY_OF_MONTH).getNextCheckMillis(date)));
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.WEEK_OF_YEAR).getNextCheckMillis(date)));
        logger.info(Timestamp.format(rollingCalendar.setType(RollingCalendar.RollingCalendarType.MONTH).getNextCheckMillis(date)));

        Assert.assertTrue(Clock.currentTimeMillis() > time);
    }
}