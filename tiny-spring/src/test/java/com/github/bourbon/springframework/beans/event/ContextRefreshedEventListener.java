package com.github.bourbon.springframework.beans.event;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.springframework.context.ApplicationListener;
import com.github.bourbon.springframework.context.event.ContextRefreshedEvent;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 11:05
 */
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextRefreshedEventListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("刷新事件：" + getClass().getName());
    }
}