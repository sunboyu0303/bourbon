package com.github.bourbon.bytecode.bytebuddy.support;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.ListUtils;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 20:04
 */
public class DefaultMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMonitor.class);

    @RuntimeType
    public static Object intercept(@SuperCall Callable<?> callable, @AllArguments Object[] args, @Argument(0) Object uid,
                                   @This Object thisObj, @Super Object parentObj, @Origin Method method) throws Exception {
        long start = Clock.currentTimeMillis();
        try {
            return callable.call();
        } finally {
            LOGGER.info("@AllArguments获取所有参数：{}", ListUtils.newArrayList(args));
            LOGGER.info("@Super父类对象结果：{}", parentObj);
            LOGGER.info("@Origin方法名称：{}", method.getName());
            LOGGER.info("@Origin入参个数：{}", method.getParameterCount());
            LOGGER.info("@Origin入参类型：{}", ListUtils.newArrayList(method.getParameterTypes()));
            LOGGER.info("@Origin出参类型：{}", method.getReturnType());
            LOGGER.info("方法耗时：{} ms", Clock.currentTimeMillis() - start);
        }
    }
}