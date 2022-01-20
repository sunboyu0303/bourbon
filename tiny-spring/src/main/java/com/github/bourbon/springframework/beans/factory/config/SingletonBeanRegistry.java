package com.github.bourbon.springframework.beans.factory.config;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 17:58
 */
@SPI(value = "default", scope = ExtensionScope.FRAMEWORK)
public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);

    void registerSingleton(String beanName, Object singletonObject);
}