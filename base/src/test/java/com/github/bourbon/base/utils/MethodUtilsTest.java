package com.github.bourbon.base.utils;

import com.github.bourbon.base.lang.counter.LongCounter;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/3 14:32
 */
public class MethodUtilsTest {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        Method[] methods = LongCounter.class.getDeclaredMethods();
        Arrays.stream(methods).forEach(method -> {
            LOGGER.info(MethodUtils.resolveMethodName(method, true));
        });
        Assert.assertNotNull(methods);
    }
}