package com.github.bourbon.common.logger.log4j;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.MessageFormatter;
import org.apache.log4j.Level;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 15:42
 */
class Log4jLogger implements Logger {

    private final org.apache.log4j.Logger logger;

    Log4jLogger(org.apache.log4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        log(Level.TRACE, msg);
    }

    @Override
    public void trace(String format, Object... arguments) {
        log(Level.TRACE, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void trace(Object obj) {
        log(Level.TRACE, obj);
    }

    @Override
    public void trace(Throwable e) {
        log(Level.TRACE, e);
    }

    @Override
    public void trace(String msg, Throwable e) {
        log(Level.TRACE, msg, e);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        log(Level.DEBUG, msg);
    }

    @Override
    public void debug(String format, Object... arguments) {
        log(Level.DEBUG, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void debug(Object obj) {
        log(Level.DEBUG, obj);
    }

    @Override
    public void debug(Throwable e) {
        log(Level.DEBUG, e);
    }

    @Override
    public void debug(String msg, Throwable e) {
        log(Level.DEBUG, msg, e);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        log(Level.INFO, msg);
    }

    @Override
    public void info(String format, Object... arguments) {
        log(Level.INFO, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void info(Object obj) {
        log(Level.INFO, obj);
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
        return logger.isEnabledFor(Level.WARN);
    }

    @Override
    public void warn(String msg) {
        log(Level.WARN, msg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        log(Level.WARN, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void warn(Object obj) {
        log(Level.WARN, obj);
    }

    @Override
    public void warn(Throwable e) {
        log(Level.WARN, e);
    }

    @Override
    public void warn(String msg, Throwable e) {
        log(Level.WARN, msg, e);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isEnabledFor(Level.ERROR);
    }

    @Override
    public void error(String msg) {
        log(Level.ERROR, msg);
    }

    @Override
    public void error(String format, Object... arguments) {
        log(Level.ERROR, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void error(Object obj) {
        log(Level.ERROR, obj);
    }

    @Override
    public void error(Throwable e) {
        log(Level.ERROR, e);
    }

    @Override
    public void error(String msg, Throwable e) {
        log(Level.ERROR, msg, e);
    }

    private void log(Level level, String msg) {
        log(level, msg, null);
    }

    private void log(Level level, Object obj) {
        log(level, obj, null);
    }

    private void log(Level level, Throwable e) {
        log(level, e.getMessage(), e);
    }

    private void log(Level level, Object obj, Throwable e) {
        logger.log(logger.getName(), level, obj, e);
    }
}