package com.github.bourbon.pfinder.profiler.sdk.util;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 12:00
 */
public class MetricNameUtilTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        logger.info(MetricNameUtil.name("a", "b", "c", "d", "e", "f"));
        logger.info(MetricNameUtil.name(MetricNameUtilTest.class, "a", "b", "c", "d", "e", "f"));
        logger.info(MetricNameUtil.name(MetricNameUtilTest.class));

        Arrays.stream(MetricNameUtil.class.getDeclaredConstructors()).forEach(c -> {
            try {
                logger.info(c);
                c.setAccessible(true);
                c.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}