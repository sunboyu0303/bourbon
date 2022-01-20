package com.github.bourbon.springframework.context.support;

import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.config.BeanPostProcessor;
import com.github.bourbon.springframework.context.ApplicationContext;
import com.github.bourbon.springframework.context.ApplicationContextAware;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 15:40
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}