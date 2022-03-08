package com.github.bourbon.pfinder.profiler.logging.basic;

import com.github.bourbon.pfinder.profiler.logging.LogFilter;
import com.github.bourbon.pfinder.profiler.logging.LogLevel;
import com.github.bourbon.pfinder.profiler.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 23:30
 */
public class PFinderLogger implements Logger {
    private final String className;
    private final LogPrinter logPrinter;
    private final PFinderLoggerConfig config;

    public PFinderLogger(Class<?> clazz, LogPrinter logPrinter, PFinderLoggerConfig config) {
        this(clazz.getName(), logPrinter, config);
    }

    public PFinderLogger(String className, LogPrinter logPrinter, PFinderLoggerConfig config) {
        this.className = className;
        this.logPrinter = logPrinter;
        this.config = config;
    }

    private void printLog(LogLevel logLevel, String message, Throwable t) {
        if (!LogFilter.FILTERS.isEmpty()) {
            for (LogFilter filter : LogFilter.FILTERS) {
                if (!filter.filter(this.className, logLevel, message, t)) {
                    return;
                }
            }
        }
        this.logPrinter.print(this.format(logLevel, message, t));
    }

    @Override
    public void log(LogLevel logLevel, String message, Object[] arguments, Throwable t) {
        if (arguments != null && arguments.length != 0) {
            this.printLog(logLevel, this.replaceParam(message, arguments, arguments.length), t);
        } else {
            this.printLog(logLevel, message, t);
        }
    }

    private void log(LogLevel logLevel, String message, Object[] arguments) {
        Throwable t = null;
        int argumentsCount = arguments.length;
        if (argumentsCount > 0) {
            Object lastArgument = arguments[argumentsCount - 1];
            if (lastArgument instanceof Throwable) {
                t = (Throwable) lastArgument;
                --argumentsCount;
            }
        }
        this.printLog(logLevel, this.replaceParam(message, arguments, argumentsCount), t);
    }

    private String format(LogLevel logLevel, String message, Throwable t) {
        return join(' ', logLevel.name(), (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(new Date()), this.className, "[", Thread.currentThread().getName(), "] :", message, t == null ? "" : this.format(t));
    }

    private String format(Throwable t) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        t.printStackTrace(new PrintWriter(buf, true));
        String expMessage = buf.toString();
        try {
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getSystemLineBreak() + expMessage;
    }

    private String replaceParam(String message, Object[] arguments, int limit) {
        if (limit <= 0) {
            return message;
        } else {
            int startSize = 0;
            int argsIndex = 0;
            int index;
            String tmpMessage;
            for (tmpMessage = message; (index = message.indexOf("{}", startSize)) != -1 && argsIndex < limit; startSize = index + 2) {
                tmpMessage = tmpMessage.replaceFirst("\\{\\}", Matcher.quoteReplacement(String.valueOf(arguments[argsIndex++])));
            }
            return tmpMessage;
        }
    }

    @Override
    public void info(String format) {
        if (this.isInfoEnabled()) {
            this.printLog(LogLevel.INFO, format, null);
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (this.isInfoEnabled()) {
            this.log(LogLevel.INFO, format, arguments);
        }
    }

    @Override
    public void info(String format, Throwable e) {
        if (this.isInfoEnabled()) {
            this.printLog(LogLevel.INFO, format, e);
        }
    }

    @Override
    public void warn(String format) {
        if (this.isWarnEnabled()) {
            this.printLog(LogLevel.WARN, format, null);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (this.isWarnEnabled()) {
            this.log(LogLevel.WARN, format, arguments);
        }
    }

    @Override
    public void warn(String format, Throwable e) {
        if (this.isWarnEnabled()) {
            this.printLog(LogLevel.WARN, format, e);
        }
    }

    @Override
    public void error(String format) {
        if (this.isErrorEnabled()) {
            this.printLog(LogLevel.ERROR, format, null);
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (this.isErrorEnabled()) {
            this.log(LogLevel.ERROR, format, arguments);
        }
    }

    @Override
    public void error(String format, Throwable e) {
        if (this.isErrorEnabled()) {
            this.printLog(LogLevel.ERROR, format, e);
        }
    }

    @Override
    public void debug(String format) {
        if (this.isDebugEnabled()) {
            this.printLog(LogLevel.DEBUG, format, null);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (this.isDebugEnabled()) {
            this.log(LogLevel.DEBUG, format, arguments);
        }
    }

    @Override
    public void debug(String format, Throwable e) {
        if (this.isDebugEnabled()) {
            this.printLog(LogLevel.DEBUG, format, e);
        }
    }

    @Override
    public void trace(String format) {
        if (this.isTraceEnabled()) {
            this.printLog(LogLevel.TRACE, format, null);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (this.isTraceEnabled()) {
            this.log(LogLevel.TRACE, format, arguments);
        }
    }

    @Override
    public void trace(String format, Throwable e) {
        if (this.isTraceEnabled()) {
            this.printLog(LogLevel.TRACE, format, e);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return LogLevel.DEBUG.compareTo(this.config.logLevel()) >= 0;
    }

    @Override
    public boolean isInfoEnabled() {
        return LogLevel.INFO.compareTo(this.config.logLevel()) >= 0;
    }

    @Override
    public boolean isWarnEnabled() {
        return LogLevel.WARN.compareTo(this.config.logLevel()) >= 0;
    }

    @Override
    public boolean isErrorEnabled() {
        return LogLevel.ERROR.compareTo(this.config.logLevel()) >= 0;
    }

    @Override
    public boolean isTraceEnabled() {
        return LogLevel.TRACE.compareTo(this.config.logLevel()) >= 0;
    }

    private static String getSystemLineBreak() {
        String osName = System.getProperty("os.name");
        String lineBreak;
        if ("linux".contains(osName)) {
            lineBreak = "\n";
        } else if ("mac".contains(osName)) {
            lineBreak = "\r";
        } else if ("win".contains(osName)) {
            lineBreak = "\n\r";
        } else {
            lineBreak = "\n";
        }
        return lineBreak;
    }

    private static String join(char delimiter, String... strings) {
        if (strings.length == 0) {
            return null;
        }
        if (strings.length == 1) {
            return strings[0];
        }
        StringBuilder sb = new StringBuilder();
        sb.append(strings[0]);
        for (int i = 1; i < strings.length; ++i) {
            String str = strings[i];
            if (str != null && !str.isEmpty()) {
                sb.append(delimiter).append(strings[i]);
            } else {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }
}