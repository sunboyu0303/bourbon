package com.github.bourbon.springframework.beans.factory.config;

import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.PropertyValues;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 17:06
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;

    default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }
}