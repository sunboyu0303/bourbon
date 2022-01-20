package com.github.bourbon.springframework.beans.factory.config;

import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.ConfigurableListableBeanFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 18:02
 */
public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}