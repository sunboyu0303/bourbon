package com.github.bourbon.springframework.beans.factory.config;

import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.BeanFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 18:03
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}