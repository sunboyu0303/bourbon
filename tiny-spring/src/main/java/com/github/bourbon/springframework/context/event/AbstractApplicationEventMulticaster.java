package com.github.bourbon.springframework.context.event;

import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.BeanFactory;
import com.github.bourbon.springframework.beans.factory.BeanFactoryAware;
import com.github.bourbon.springframework.context.ApplicationEvent;
import com.github.bourbon.springframework.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 10:13
 */
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    private final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    protected BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<ApplicationEvent> listener) {
        applicationListeners.add(listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<ApplicationEvent> listener) {
        applicationListeners.remove(listener);
    }

    protected Collection<ApplicationListener<ApplicationEvent>> getApplicationListeners(ApplicationEvent e) {
        return applicationListeners.stream().filter(l -> supportsEvent(l, e)).collect(Collectors.toList());
    }

    private boolean supportsEvent(ApplicationListener<ApplicationEvent> listener, ApplicationEvent event) {
        Class<? extends ApplicationListener> listenerClass = listener.getClass();
        Class<?> targetClass = ClassUtils.isCglibProxyClass(listenerClass) ? listenerClass.getSuperclass() : listenerClass;
        String className = ((ParameterizedType) targetClass.getGenericInterfaces()[0]).getActualTypeArguments()[0].getTypeName();
        try {
            return Class.forName(className).isAssignableFrom(event.getClass());
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + className);
        }
    }
}