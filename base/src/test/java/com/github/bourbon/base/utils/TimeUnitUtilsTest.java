package com.github.bourbon.base.utils;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/11 14:38
 */
public class TimeUnitUtilsTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void getTimeUnit() {
        TimeUnit minutes = TimeUnitUtils.getTimeUnit("minutes", null);
        logger.info(minutes);
        Assert.assertNull(minutes);
        TimeUnit MINUTES = TimeUnitUtils.getTimeUnit("MINUTES", null);
        logger.info(MINUTES);
        Assert.assertNotNull(MINUTES);
        long start = Clock.currentTimeMillis();
        TimeUnitUtils.sleepSeconds(1);
        logger.error("use time: {}", (Clock.currentTimeMillis() - start));
        start = Clock.currentTimeMillis();
        TimeUnitUtils.sleepMilliSeconds(33);
        logger.error("use time: {}", (Clock.currentTimeMillis() - start));
    }
}