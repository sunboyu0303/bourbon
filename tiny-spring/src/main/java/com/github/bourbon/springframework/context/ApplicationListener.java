package com.github.bourbon.springframework.context;

import java.util.EventListener;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 10:04
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    void onApplicationEvent(E event);
}