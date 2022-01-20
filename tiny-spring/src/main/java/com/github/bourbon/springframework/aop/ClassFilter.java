package com.github.bourbon.springframework.aop;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 15:36
 */
public interface ClassFilter {

    boolean matches(Class<?> clazz);
}