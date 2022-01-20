package com.github.bourbon.springframework.context.event;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 10:22
 */
public class ContextClosedEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = -3057032609432768474L;

    public ContextClosedEvent(Object source) {
        super(source);
    }
}