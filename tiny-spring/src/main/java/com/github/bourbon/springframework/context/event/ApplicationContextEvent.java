package com.github.bourbon.springframework.context.event;

import com.github.bourbon.springframework.context.ApplicationContext;
import com.github.bourbon.springframework.context.ApplicationEvent;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 10:21
 */
public class ApplicationContextEvent extends ApplicationEvent {

    private static final long serialVersionUID = -8873748715287103728L;

    public ApplicationContextEvent(Object source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}