package com.github.bourbon.springframework.beans.factory.config;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

import java.beans.PropertyDescriptor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/6 17:31
 */
public interface CacheAnnotationBeanPostProcessor extends SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware {

    @Override
    @Deprecated
    default PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) {
        return postProcessProperties(pvs, bean, beanName);
    }
}