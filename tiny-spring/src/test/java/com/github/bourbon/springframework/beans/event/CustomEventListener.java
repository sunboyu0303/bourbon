package com.github.bourbon.springframework.beans.event;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.springframework.context.ApplicationListener;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 11:06
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomEventListener.class);

    @Override
    public void onApplicationEvent(CustomEvent event) {
        LOGGER.info("收到：" + event.getSource() + " 消息;时间: " + Clock.currentTimeMillis());
        LOGGER.info("消息：" + event.getId() + " :" + event.getMessage());
    }
}