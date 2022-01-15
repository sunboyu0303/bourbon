package com.github.bourbon.common.logger.jdk;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.MessageFormatter;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.logging.Level;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 15:32
 */
class JdkLogger implements Logger {

    private final java.util.logging.Logger logger;

    JdkLogger(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isLoggable(Level.FINER);
    }

    @Override
    public void trace(String msg) {
        log(Level.FINER, msg);
    }

    @Override
    public void trace(String format, Object... arguments) {
        trace(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void trace(Object obj) {
        trace(ObjectUtils.toString(obj));
    }

    @Override
    public void trace(Throwable e) {
        log(Level.FINER, e);
    }

    @Override
    public void trace(String msg, Throwable e) {
        log(Level.FINER, msg, e);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE);
    }

    @Override
    public void debug(String msg) {
        log(Level.FINE, msg);
    }

    @Override
    public void debug(String format, Object... arguments) {
        debug(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void debug(Object obj) {
        debug(ObjectUtils.toString(obj));
    }

    @Override
    public void debug(Throwable e) {
        log(Level.FINE, e);
    }

    @Override
    public void debug(String msg, Throwable e) {
        log(Level.FINE, msg, e);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    @Override
    public void info(String msg) {
        log(Level.INFO, msg);
    }

    @Override
    public void info(String format, Object... arguments) {
        info(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void info(Object obj) {
        info(ObjectUtils.toString(obj));
    }

    @Override
    public void info(Throwable e) {
        log(Level.INFO, e);
    }

    @Override
    public void info(String msg, Throwable e) {
        log(Level.INFO, msg, e);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    @Override
    public void warn(String msg) {
        log(Level.WARNING, msg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        warn(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void warn(Object obj) {
        warn(ObjectUtils.toString(obj));
    }

    @Override
    public void warn(Throwable e) {
        log(Level.WARNING, e);
    }

    @Override
    public void warn(String msg, Throwable e) {
        log(Level.WARNING, msg, e);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

    @Override
    public void error(String msg) {
        log(Level.SEVERE, msg);
    }

    @Override
    public void error(String format, Object... arguments) {
        error(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void error(Object obj) {
        error(ObjectUtils.toString(obj));
    }

    @Override
    public void error(Throwable e) {
        log(Level.SEVERE, e);
    }

    @Override
    public void error(String msg, Throwable e) {
        log(Level.SEVERE, msg, e);
    }

    private void log(Level level, String msg) {
        logger.log(level, msg);
    }

    private void log(Level level, Throwable e) {
        log(level, e.getMessage(), e);
    }

    private void log(Level level, String msg, Throwable e) {
        logger.log(level, msg, e);
    }
}