package com.github.bourbon.springframework.context.event;

import com.github.bourbon.springframework.beans.factory.BeanFactory;
import com.github.bourbon.springframework.context.ApplicationEvent;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 10:23
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        getApplicationListeners(event).forEach(listener -> listener.onApplicationEvent(event));
    }
}