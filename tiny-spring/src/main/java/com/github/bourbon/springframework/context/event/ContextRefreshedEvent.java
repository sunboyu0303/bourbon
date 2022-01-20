package com.github.bourbon.springframework.context.event;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 10:22
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = -288763296272675431L;

    public ContextRefreshedEvent(Object source) {
        super(source);
    }
}