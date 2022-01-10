package com.github.bourbon.bytecode.bytebuddy;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 20:04
 */
public class TestMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestMonitor.class);

    @RuntimeType
    public static Object intercept(@SuperCall Callable<?> callable,
                                   @AllArguments Object[] args,
                                   @Argument(0) Object uid,
                                   @This Object thisObj,
                                   @Super Object parentObj,
                                   @Origin Method method) throws Exception {
        long start = Clock.currentTimeMillis();
        try {
            return callable.call();
        } finally {
            LOGGER.info("方法耗时：{} ms", Clock.currentTimeMillis() - start);
        }
    }
}