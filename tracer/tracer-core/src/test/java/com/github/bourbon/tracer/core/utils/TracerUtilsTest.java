package com.github.bourbon.tracer.core.utils;

import com.github.bourbon.base.appender.config.LogReserveConfig;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/10 11:11
 */
public class TracerUtilsTest {

    @Test
    public void test() {
        LogReserveConfig config = TracerUtils.parseLogReserveConfig("7D");
        Assert.assertTrue(config.getDay() == 7 && config.getHour() == 0);
        config = TracerUtils.parseLogReserveConfig("7D0H");
        Assert.assertTrue(config.getDay() == 7 && config.getHour() == 0);
        config = TracerUtils.parseLogReserveConfig("7");
        Assert.assertTrue(config.getDay() == 7 && config.getHour() == 0);
    }
}