package com.github.bourbon.springframework.context;

import java.util.EventObject;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 10:03
 */
public abstract class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = -4159027418608906229L;

    protected ApplicationEvent(Object source) {
        super(source);
    }
}