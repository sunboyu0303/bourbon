package com.github.bourbon.springframework.beans.factory;

import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.github.bourbon.springframework.beans.factory.config.BeanDefinition;
import com.github.bourbon.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 18:02
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;
}