package com.github.bourbon.springframework.beans.event;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.springframework.context.ApplicationListener;
import com.github.bourbon.springframework.context.event.ContextClosedEvent;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 11:03
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextClosedEventListener.class);

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        LOGGER.info("关闭事件：" + getClass().getName());
    }
}