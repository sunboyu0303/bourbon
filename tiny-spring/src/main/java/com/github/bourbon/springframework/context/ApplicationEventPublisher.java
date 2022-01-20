package com.github.bourbon.springframework.context;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 10:03
 */
public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
}