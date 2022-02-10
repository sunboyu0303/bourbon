package com.github.bourbon.tracer.core.appender.file;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.RollingCalendar;
import com.github.bourbon.base.utils.TimeUnitUtils;
import com.github.bourbon.tracer.core.appender.self.TracerDaemon;
import com.github.bourbon.tracer.core.utils.TracerUtils;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 19:52
 */
public class TimedRollingFileAppenderTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() throws IOException {
        TracerDaemon.start();
        TimedRollingFileAppender appender = new TimedRollingFileAppender("/test.log", TimedRollingFileAppender.DAILY_ROLLING_PATTERN, "7");
        appender.cleanup();
        for (int i = 0; i < 3600; i++) {
            appender.append(ThreadLocalRandom.current().nextInt(1231231231) + StringConstants.NEWLINE);
            TimeUnitUtils.sleepSeconds(1);
        }
    }

    @Test
    public void testComputeCheckPeriod() {
        Date epoch = new Date(0);
        RollingCalendar rollingCalendar = new RollingCalendar(TracerUtils.getGMTDefaultTimeZone(), Locale.getDefault());
        for (RollingCalendar.RollingCalendarType type : TimedRollingFileAppender.TYPE_LIST) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimedRollingFileAppender.DAILY_ROLLING_PATTERN);
            simpleDateFormat.setTimeZone(TracerUtils.getGMTDefaultTimeZone());
            String r0 = simpleDateFormat.format(epoch);
            rollingCalendar.setType(type);
            String r1 = simpleDateFormat.format(new Date(rollingCalendar.getNextCheckMillis(epoch)));
            if (!r0.equals(r1)) {
                logger.info("enum: {}, r0: {}, r1: {}", type, r0, r1);
                break;
            }
        }
        for (RollingCalendar.RollingCalendarType type : TimedRollingFileAppender.TYPE_LIST) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimedRollingFileAppender.HOURLY_ROLLING_PATTERN);
            simpleDateFormat.setTimeZone(TracerUtils.getGMTDefaultTimeZone());
            String r0 = simpleDateFormat.format(epoch);
            rollingCalendar.setType(type);
            String r1 = simpleDateFormat.format(new Date(rollingCalendar.getNextCheckMillis(epoch)));
            if (!r0.equals(r1)) {
                logger.info("enum: {}, r0: {}, r1: {}", type, r0, r1);
                break;
            }
        }
    }
}