package com.github.bourbon.springframework.aop;

import java.lang.reflect.Method;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 15:35
 */
public interface MethodMatcher {

    boolean matches(Method method, Class<?> targetClass);
}