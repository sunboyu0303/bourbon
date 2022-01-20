package com.github.bourbon.springframework.beans.factory.support;

import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.DisposableBean;
import com.github.bourbon.springframework.beans.factory.ObjectFactory;
import com.github.bourbon.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 21:50
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * 一级缓存，普通对象。
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    /**
     * 二级缓存，提前暴漏对象，没有完全实例化的对象。
     */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();

    /**
     * 三级缓存，存放代理对象。
     */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();

    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        if (null == singletonObject) {
            singletonObject = earlySingletonObjects.get(beanName);
            if (null == singletonObject) {
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        if (!singletonObjects.containsKey(beanName)) {
            singletonFactories.put(beanName, singletonFactory);
            earlySingletonObjects.remove(beanName);
        }
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    public void destroySingletons() {
        for (Map.Entry<String, DisposableBean> entry : disposableBeans.entrySet()) {
            try {
                entry.getValue().destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + entry.getKey() + "' threw an exception", e);
            }
        }
    }
}