package com.github.bourbon.common.instantiate;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

import java.lang.reflect.Constructor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 01:00
 */
@SPI(value = "simple", scope = ExtensionScope.FRAMEWORK)
public interface InstantiationStrategy {

    Object instantiate(Class<?> clazz, Constructor<?> ctor, Object[] args) throws Exception;
}