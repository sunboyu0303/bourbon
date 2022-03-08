package com.github.bourbon.pfinder.profiler.logging;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 23:03
 */
public interface Logger {

    void info(String var1);

    void info(String var1, Object... var2);

    void info(String var1, Throwable var2);

    void warn(String var1);

    void warn(String var1, Object... var2);

    void warn(String var1, Throwable var2);

    void error(String var1);

    void error(String var1, Object... var2);

    void error(String var1, Throwable var2);

    void debug(String var1);

    void debug(String var1, Object... var2);

    void debug(String var1, Throwable var2);

    void trace(String var1);

    void trace(String var1, Object... var2);

    void trace(String var1, Throwable var2);

    boolean isDebugEnabled();

    boolean isInfoEnabled();

    boolean isWarnEnabled();

    boolean isErrorEnabled();

    boolean isTraceEnabled();

    void log(LogLevel var1, String var2, Object[] var3, Throwable var4);
}