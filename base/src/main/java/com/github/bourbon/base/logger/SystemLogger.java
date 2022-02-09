package com.github.bourbon.base.logger;

import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.base.utils.Timestamp;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/2 10:30
 */
public class SystemLogger implements Logger {

    private static final String FORMAT = "%s [%s] %s %s %s - %s";

    @Override
    public boolean isTraceEnabled() {
        return isWarnEnabled();
    }

    @Override
    public void trace(String msg) {
        out(getMessage(Level.TRACE, msg));
    }

    @Override
    public void trace(String format, Object... arguments) {
        out(getMessage(Level.TRACE, MessageFormatter.arrayFormat(format, arguments).getMessage()));
    }

    @Override
    public void trace(Object obj) {
        out(getMessage(Level.TRACE, obj));
    }

    @Override
    public void trace(Throwable e) {
        out(getMessage(Level.TRACE, e.toString()));
        e.printStackTrace();
    }

    @Override
    public void trace(String msg, Throwable e) {
        out(getMessage(Level.TRACE, msg));
        e.printStackTrace();
    }

    @Override
    public boolean isDebugEnabled() {
        return isWarnEnabled();
    }

    @Override
    public void debug(String msg) {
        out(getMessage(Level.DEBUG, msg));
    }

    @Override
    public void debug(String format, Object... arguments) {
        out(getMessage(Level.DEBUG, MessageFormatter.arrayFormat(format, arguments).getMessage()));
    }

    @Override
    public void debug(Object obj) {
        out(getMessage(Level.DEBUG, obj));
    }

    @Override
    public void debug(Throwable e) {
        out(getMessage(Level.DEBUG, e.toString()));
        e.printStackTrace();
    }

    @Override
    public void debug(String msg, Throwable e) {
        out(getMessage(Level.DEBUG, msg));
        e.printStackTrace();
    }

    @Override
    public boolean isInfoEnabled() {
        return isWarnEnabled();
    }

    @Override
    public void info(String msg) {
        out(getMessage(Level.INFO, msg));
    }

    @Override
    public void info(String format, Object... arguments) {
        out(getMessage(Level.INFO, MessageFormatter.arrayFormat(format, arguments).getMessage()));
    }

    @Override
    public void info(Object obj) {
        out(getMessage(Level.INFO, obj));
    }

    @Override
    public void info(Throwable e) {
        out(getMessage(Level.INFO, e.toString()));
        e.printStackTrace();
    }

    @Override
    public void info(String msg, Throwable e) {
        out(getMessage(Level.INFO, msg));
        e.printStackTrace();
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(String msg) {
        out(getMessage(Level.WARN, msg));
    }

    @Override
    public void warn(String format, Object... arguments) {
        out(getMessage(Level.WARN, MessageFormatter.arrayFormat(format, arguments).getMessage()));
    }

    @Override
    public void warn(Object obj) {
        out(getMessage(Level.WARN, obj));
    }

    @Override
    public void warn(Throwable e) {
        out(getMessage(Level.WARN, e.toString()));
        e.printStackTrace();
    }

    @Override
    public void warn(String msg, Throwable e) {
        out(getMessage(Level.WARN, msg));
        e.printStackTrace();
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(String msg) {
        err(getMessage(Level.ERROR, msg));
    }

    @Override
    public void error(String format, Object... arguments) {
        err(getMessage(Level.ERROR, MessageFormatter.arrayFormat(format, arguments).getMessage()));
    }

    @Override
    public void error(Object obj) {
        err(getMessage(Level.ERROR, obj));
    }

    @Override
    public void error(Throwable e) {
        err(getMessage(Level.ERROR, e.toString()));
        e.printStackTrace();
    }

    @Override
    public void error(String msg, Throwable e) {
        err(getMessage(Level.ERROR, msg));
        e.printStackTrace();
    }

    private void out(Object o) {
        System.out.println(o);
    }

    private void err(Object o) {
        System.err.println(o);
    }

    private String getMessage(Level level, Object o) {
        // 2021-12-02 11:03:44.177 [Thread-19] INFO c.j.o.p.c.c.s.i.NetServiceImpl 240 -
        Thread t = Thread.currentThread();
        StackTraceElement e = t.getStackTrace()[4];
        return String.format(FORMAT, Timestamp.currentTime(), t.getName(), level.name(), ClassUtils.getShortClassName(e.getClassName()), e.getLineNumber(), o);
    }
}