package com.github.bourbon.springframework.beans.factory;

import com.github.bourbon.springframework.beans.BeansException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 16:04
 */
public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;

    Object getBean(String beanName, Object... args) throws BeansException;

    <T> T getBean(String beanName, Class<T> type) throws BeansException;

    <T> T getBean(Class<T> type) throws BeansException;

    boolean containsBean(String beanName);
}