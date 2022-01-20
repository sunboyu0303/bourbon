package com.github.bourbon.springframework.aop;

import java.lang.reflect.Method;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 17:09
 */
public interface MethodBeforeAdvice extends BeforeAdvice {

    void before(Method method, Object[] args, Object target) throws Throwable;
}