package com.github.bourbon.pfinder.profiler.logging.slf4j;

import com.github.bourbon.pfinder.profiler.logging.LogFilter;
import com.github.bourbon.pfinder.profiler.logging.LogLevel;
import com.github.bourbon.pfinder.profiler.logging.Logger;
import com.github.bourbon.pfinder.profiler.logging.LoggerFactory;
import com.github.bourbon.pfinder.profiler.logging.basic.PFinderLoggerConfig;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 00:06
 */
public class Slf4jLoggerProxy implements Logger {
    private final LocationAwareLogger logger;
    private final PFinderLoggerConfig config;
    private final String className;
    private final Logger systemOutLogger;
    private final AtomicBoolean useSystemOutLogger = new AtomicBoolean(false);
    private static final Object[] EMPTY_ARGS = new Object[0];

    Slf4jLoggerProxy(Class<?> clazz, PFinderLoggerConfig config, Logger systemOutLogger) {
        this.logger = (LocationAwareLogger) LoggerFactory.getLogger(clazz);
        this.className = clazz.getName();
        this.systemOutLogger = systemOutLogger;
        this.config = config;
    }

    private void log(String className, LogLevel logLevel, String format, Object[] args, Throwable t) {
        if (this.useSystemOutLogger.get()) {
            this.systemOutLogger.log(logLevel, format, args, t);
            return;
        }
        try {
            if (logLevel.compareTo(this.config.logLevel()) < 0) {
                return;
            }

            FormattingTuple ft = t == null ? MessageFormatter.arrayFormat(format, args) : MessageFormatter.arrayFormat(format, args, t);
            String message = ft.getMessage();
            Throwable throwable = ft.getThrowable();
            if (!LogFilter.FILTERS.isEmpty()) {
                for (LogFilter filter : LogFilter.FILTERS) {
                    if (!filter.filter(className, logLevel, message, throwable)) {
                        return;
                    }
                }
            }

            this.logger.log(null, className, this.convertLevel(logLevel), message, null, throwable);
        } catch (Throwable var11) {
            System.out.println("PFinder slf4j proxy print log error! Probably because the slf4j-api version is too low, make sure >= 1.7.25!");
            this.useSystemOutLogger.set(true);
        }
    }

    private int convertLevel(LogLevel logLevel) {
        switch (logLevel) {
            case TRACE:
                return 0;
            case DEBUG:
                return 10;
            case INFO:
                return 20;
            case WARN:
                return 30;
            case ERROR:
                return 40;
            default:
                return 10;
        }
    }

    private String getTraceClass(int deep) {
        StackTraceElement[] elements = (new Throwable()).getStackTrace();
        return elements != null && elements.length >= deep ? elements[deep].getClassName() : null;
    }

    @Override
    public void info(String format) {
        this.log(this.getTraceClass(0), LogLevel.INFO, format, null, null);
    }

    @Override
    public void info(String format, Object... arguments) {
        this.log(this.getTraceClass(0), LogLevel.INFO, format, arguments, null);
    }

    @Override
    public void info(String format, Throwable e) {
        this.log(this.getTraceClass(0), LogLevel.INFO, format, EMPTY_ARGS, e);
    }

    @Override
    public void warn(String format) {
        this.log(this.getTraceClass(0), LogLevel.WARN, format, null, null);
    }

    @Override
    public void warn(String format, Object... arguments) {
        this.log(this.getTraceClass(0), LogLevel.WARN, format, arguments, null);
    }

    @Override
    public void warn(String format, Throwable e) {
        this.log(this.getTraceClass(0), LogLevel.WARN, format, EMPTY_ARGS, e);
    }

    @Override
    public void error(String format) {
        this.log(this.getTraceClass(0), LogLevel.ERROR, format, null, null);
    }

    @Override
    public void error(String format, Object... arguments) {
        this.log(this.getTraceClass(0), LogLevel.ERROR, format, arguments, null);
    }

    @Override
    public void error(String format, Throwable e) {
        this.log(this.getTraceClass(0), LogLevel.ERROR, format, EMPTY_ARGS, e);
    }

    @Override
    public void debug(String format) {
        this.log(this.getTraceClass(0), LogLevel.DEBUG, format, null, null);
    }

    @Override
    public void debug(String format, Object... arguments) {
        this.log(this.getTraceClass(0), LogLevel.DEBUG, format, arguments, null);
    }

    @Override
    public void debug(String format, Throwable e) {
        this.log(this.getTraceClass(0), LogLevel.DEBUG, format, EMPTY_ARGS, e);
    }

    @Override
    public void trace(String format) {
        this.log(this.getTraceClass(0), LogLevel.TRACE, format, null, null);
    }

    @Override
    public void trace(String format, Object... arguments) {
        this.log(this.getTraceClass(0), LogLevel.TRACE, format, arguments, null);
    }

    @Override
    public void trace(String format, Throwable e) {
        this.log(this.getTraceClass(0), LogLevel.TRACE, format, EMPTY_ARGS, e);
    }

    @Override
    public boolean isDebugEnabled() {
        return LogLevel.DEBUG.compareTo(this.config.logLevel()) >= 0 && this.logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return LogLevel.INFO.compareTo(this.config.logLevel()) >= 0 && this.logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return LogLevel.WARN.compareTo(this.config.logLevel()) >= 0 && this.logger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return LogLevel.ERROR.compareTo(this.config.logLevel()) >= 0 && this.logger.isErrorEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return LogLevel.TRACE.compareTo(this.config.logLevel()) >= 0 && this.logger.isTraceEnabled();
    }

    @Override
    public void log(LogLevel logLevel, String message, Object[] arguments, Throwable t) {
        this.log(this.className, logLevel, message, arguments, t);
    }
}