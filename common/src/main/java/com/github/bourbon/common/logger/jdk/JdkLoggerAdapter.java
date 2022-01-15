package com.github.bourbon.common.logger.jdk;

import com.github.bourbon.base.logger.Level;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerAdapter;
import com.github.bourbon.base.logger.SystemLogger;
import com.github.bourbon.common.logger.AbstractLoggerAdapter;

import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 15:36
 */
public class JdkLoggerAdapter extends AbstractLoggerAdapter implements LoggerAdapter {

    private static final String GLOBAL_LOGGER_NAME = "global";
    private static final Logger LOGGER = new SystemLogger();

    static {
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.properties");
            if (in != null) {
                LogManager.getLogManager().readConfiguration(in);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    @Override
    public Logger getLogger(Class<?> clazz) {
        return new JdkLogger(java.util.logging.Logger.getLogger(clazz.getName()));
    }

    @Override
    public Logger getLogger(String name) {
        return new JdkLogger(java.util.logging.Logger.getLogger(name));
    }

    @Override
    public Level getLevel() {
        return fromJdkLevel(java.util.logging.Logger.getLogger(GLOBAL_LOGGER_NAME).getLevel());
    }

    @Override
    public void setLevel(Level level) {
        java.util.logging.Logger.getLogger(GLOBAL_LOGGER_NAME).setLevel(toJdkLevel(level));
    }

    @Override
    public boolean initialize() {
        return true;
    }

    @Override
    public int getPriority() {
        return MIN_PRIORITY;
    }

    private static Level fromJdkLevel(java.util.logging.Level level) {
        if (level == java.util.logging.Level.ALL) {
            return Level.ALL;
        }
        if (level == java.util.logging.Level.FINER) {
            return Level.TRACE;
        }
        if (level == java.util.logging.Level.FINE) {
            return Level.DEBUG;
        }
        if (level == java.util.logging.Level.INFO) {
            return Level.INFO;
        }
        if (level == java.util.logging.Level.WARNING) {
            return Level.WARN;
        }
        if (level == java.util.logging.Level.SEVERE) {
            return Level.ERROR;
        }
        return Level.OFF;
    }

    private static java.util.logging.Level toJdkLevel(Level level) {
        if (level == Level.ALL) {
            return java.util.logging.Level.ALL;
        }
        if (level == Level.TRACE) {
            return java.util.logging.Level.FINER;
        }
        if (level == Level.DEBUG) {
            return java.util.logging.Level.FINE;
        }
        if (level == Level.INFO) {
            return java.util.logging.Level.INFO;
        }
        if (level == Level.WARN) {
            return java.util.logging.Level.WARNING;
        }
        if (level == Level.ERROR) {
            return java.util.logging.Level.SEVERE;
        }
        return java.util.logging.Level.OFF;
    }
}