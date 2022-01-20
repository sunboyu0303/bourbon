package com.github.bourbon.springframework.context.bean.circle;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/9 14:19
 */
public class SpouseAdvice implements MethodBeforeAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpouseAdvice.class);

    @Override
    public void before(Method method, Object[] args, Object target) {
        LOGGER.info("关怀小两口(切面)：" + method);
    }
}