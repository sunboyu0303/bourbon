package com.github.bourbon.springframework.beans.factory.config;

import com.github.bourbon.springframework.beans.BeansException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 18:00
 */
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}