package com.github.bourbon.base.utils;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/1 15:06
 */
public class DateUtilsTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testDateUtils() {
        LocalDateTime localDateTime = LocalDateTimeUtils.localDateTime(2020, 11, 1, 0, 0);
        logger.info(LocalDateTimeUtils.toMillis(localDateTime));
        Assert.assertTrue(LocalDateTimeUtils.toSeconds(localDateTime) > 0);
    }
}