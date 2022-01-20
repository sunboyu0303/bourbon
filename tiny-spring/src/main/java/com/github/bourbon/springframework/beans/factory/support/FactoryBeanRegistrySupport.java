package com.github.bourbon.springframework.beans.factory.support;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 17:43
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    private static final Object EMPTY = new Object();

    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object object = factoryBeanObjectCache.get(beanName);
        return (ObjectUtils.isNull(object) || EMPTY == object) ? null : object;
    }

    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName) {
        return !factory.isSingleton() ? doGetObjectFromFactoryBean(factory, beanName) : factoryBeanObjectCache.computeIfAbsent(beanName, k -> ObjectUtils.defaultIfNull(doGetObjectFromFactoryBean(factory, beanName), EMPTY));
    }

    private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, final String beanName) {
        try {
            return factory.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
        }
    }
}