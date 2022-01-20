package com.github.bourbon.springframework.beans.factory;

import com.github.bourbon.springframework.beans.BeansException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 16:07
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}