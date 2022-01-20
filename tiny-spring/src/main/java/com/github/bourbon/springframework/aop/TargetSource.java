package com.github.bourbon.springframework.aop;

import com.github.bourbon.base.utils.ClassUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 15:34
 */
public class TargetSource {

    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }

    public Class<?>[] getTargetClass() {
        Class<?> clazz = target.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        return clazz.getInterfaces();
    }
}