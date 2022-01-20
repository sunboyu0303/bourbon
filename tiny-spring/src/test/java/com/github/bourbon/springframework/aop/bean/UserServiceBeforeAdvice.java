package com.github.bourbon.springframework.aop.bean;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 17:28
 */
public class UserServiceBeforeAdvice implements MethodBeforeAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceBeforeAdvice.class);

    @Override
    public void before(Method method, Object[] args, Object target) {
        LOGGER.info("拦截方法：" + method.getName());
    }
}