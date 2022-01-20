package com.github.bourbon.springframework.beans.event;

import com.alibaba.fastjson.JSON;
import com.github.bourbon.springframework.context.event.ApplicationContextEvent;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 11:03
 */
public class CustomEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = -5760236985443156013L;
    private final Long id;
    private final String message;

    public CustomEvent(Object source, Long id, String message) {
        super(source);
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}