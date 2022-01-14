package com.github.bourbon.springframework.cache.core.annotation;

import com.github.bourbon.springframework.beans.factory.config.CacheAnnotationBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/7 17:33
 */
public abstract class AbstractCacheAnnotationBeanPostProcessor implements CacheAnnotationBeanPostProcessor {

    protected ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }
}