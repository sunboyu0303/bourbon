package com.github.bourbon.base.logger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 15:31
 */
public interface Logger {

    boolean isTraceEnabled();

    void trace(String msg);

    void trace(String format, Object... arguments);

    void trace(Object obj);

    void trace(Throwable e);

    void trace(String msg, Throwable e);

    boolean isDebugEnabled();

    void debug(String msg);

    void debug(String format, Object... arguments);

    void debug(Object obj);

    void debug(Throwable e);

    void debug(String msg, Throwable e);

    boolean isInfoEnabled();

    void info(String msg);

    void info(String format, Object... arguments);

    void info(Object obj);

    void info(Throwable e);

    void info(String msg, Throwable e);

    boolean isWarnEnabled();

    void warn(String msg);

    void warn(String format, Object... arguments);

    void warn(Object obj);

    void warn(Throwable e);

    void warn(String msg, Throwable e);

    boolean isErrorEnabled();

    void error(String msg);

    void error(String format, Object... arguments);

    void error(Object obj);

    void error(Throwable e);

    void error(String msg, Throwable e);
}