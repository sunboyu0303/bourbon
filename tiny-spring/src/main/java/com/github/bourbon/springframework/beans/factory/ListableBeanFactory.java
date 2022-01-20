package com.github.bourbon.springframework.beans.factory;

import com.github.bourbon.springframework.beans.BeansException;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 16:05
 */
public interface ListableBeanFactory extends BeanFactory {

    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    String[] getBeanDefinitionNames();
}