package com.github.bourbon.common.instantiate.support;

import com.github.bourbon.common.instantiate.InstantiationStrategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 01:01
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(Class<?> clazz, Constructor<?> ctor, Object[] args) throws Exception {
        try {
            return ctor == null ? clazz.getDeclaredConstructor().newInstance() :
                    clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new Exception("Failed to instantiate [" + clazz.getName() + "]", e);
        }
    }
}