package com.github.bourbon.springframework.beans.factory.support;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.config.BeanDefinition;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 15:43
 */
@SPI(value = "default", scope = ExtensionScope.FRAMEWORK)
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    boolean containsBeanDefinition(String beanName);

    String[] getBeanDefinitionNames();
}