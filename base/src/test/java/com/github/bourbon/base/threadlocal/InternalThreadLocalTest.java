package com.github.bourbon.base.threadlocal;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.TimeUnitUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 15:38
 */
public class InternalThreadLocalTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static InternalThreadLocal<Integer> internalThreadLocal = new InternalThreadLocal<>();

    @Test
    public void testInternalThreadLocal() {
        new InternalThread(() -> {
            for (int i = 0; i < 5; i++) {
                internalThreadLocal.set(i);
                logger.info(internalThreadLocal.get());
            }
        }, "internalThread_have_set").start();

        new InternalThread(() -> {
            for (int i = 0; i < 5; i++) {
                logger.info(internalThreadLocal.get());
            }
        }, "internalThread_no_set").start();

        TimeUnitUtils.sleepSeconds(3L);

        Assert.assertNull(internalThreadLocal.get());
    }
}