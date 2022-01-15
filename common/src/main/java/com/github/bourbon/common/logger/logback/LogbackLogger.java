package com.github.bourbon.common.logger.logback;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 12:09
 */
class LogbackLogger implements Logger {

    private final ch.qos.logback.classic.Logger logger;

    LogbackLogger(ch.qos.logback.classic.Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format, arguments);
    }

    @Override
    public void trace(Object obj) {
        trace(ObjectUtils.toString(obj));
    }

    @Override
    public void trace(Throwable e) {
        logger.trace(e.getMessage(), e);
    }

    @Override
    public void trace(String msg, Throwable e) {
        logger.trace(msg, e);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format, arguments);
    }

    @Override
    public void debug(Object obj) {
        debug(ObjectUtils.toString(obj));
    }

    @Override
    public void debug(Throwable e) {
        logger.debug(e.getMessage(), e);
    }

    @Override
    public void debug(String msg, Throwable e) {
        logger.debug(msg, e);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format, arguments);
    }

    @Override
    public void info(Object obj) {
        info(ObjectUtils.toString(obj));
    }

    @Override
    public void info(Throwable e) {
        logger.info(e.getMessage(), e);
    }

    @Override
    public void info(String msg, Throwable e) {
        logger.info(msg, e);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format, arguments);
    }

    @Override
    public void warn(Object obj) {
        warn(ObjectUtils.toString(obj));
    }

    @Override
    public void warn(Throwable e) {
        logger.warn(e.getMessage(), e);
    }

    @Override
    public void warn(String msg, Throwable e) {
        logger.warn(msg, e);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format, arguments);
    }

    @Override
    public void error(Object obj) {
        error(ObjectUtils.toString(obj));
    }

    @Override
    public void error(Throwable e) {
        logger.error(e.getMessage(), e);
    }

    @Override
    public void error(String msg, Throwable e) {
        logger.error(msg, e);
    }
}