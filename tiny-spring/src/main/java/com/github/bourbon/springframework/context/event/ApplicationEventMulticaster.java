package com.github.bourbon.springframework.context.event;

import com.github.bourbon.springframework.context.ApplicationEvent;
import com.github.bourbon.springframework.context.ApplicationListener;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 10:11
 */
public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<ApplicationEvent> listener);

    void removeApplicationListener(ApplicationListener<ApplicationEvent> listener);

    void multicastEvent(ApplicationEvent event);
}