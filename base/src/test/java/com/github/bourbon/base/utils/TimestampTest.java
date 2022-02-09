package com.github.bourbon.base.utils;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/10 08:55
 */
public class TimestampTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        long time = 1644454581001L;
        logger.info(Timestamp.currentTime());
        logger.info(Timestamp.format(time));
        Assert.assertTrue(Clock.currentTimeMillis() > time);
    }
}