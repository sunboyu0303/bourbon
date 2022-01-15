package com.github.bourbon.common.logger.slf4j;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerAdapter;
import com.github.bourbon.common.logger.AbstractLoggerAdapter;
import org.slf4j.LoggerFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 11:38
 */
public class Slf4jLoggerAdapter extends AbstractLoggerAdapter implements LoggerAdapter {

    @Override
    public Logger getLogger(Class<?> clazz) {
        return new Slf4jLogger(LoggerFactory.getLogger(clazz));
    }

    @Override
    public Logger getLogger(String name) {
        return new Slf4jLogger(LoggerFactory.getLogger(name));
    }

    @Override
    public boolean initialize() {
        try {
            Class.forName("org.slf4j.LoggerFactory");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 400;
    }
}