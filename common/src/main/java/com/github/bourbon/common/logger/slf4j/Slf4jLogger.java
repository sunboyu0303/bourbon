package com.github.bourbon.common.logger.slf4j;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.MessageFormatter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.slf4j.spi.LocationAwareLogger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 11:46
 */
class Slf4jLogger implements Logger {

    private final org.slf4j.Logger log;

    private final LocationAwareLogger laLog;

    Slf4jLogger(org.slf4j.Logger log) {
        this.laLog = BooleanUtils.defaultIfAssignableFrom(log, LocationAwareLogger.class, LocationAwareLogger.class::cast);
        this.log = log;
    }

    @Override
    public boolean isTraceEnabled() {
        return ObjectUtils.defaultIfNull(laLog, org.slf4j.Logger::isTraceEnabled, log.isTraceEnabled());
    }

    @Override
    public void trace(String msg) {
        if (laLog != null) {
            log(LocationAwareLogger.TRACE_INT, msg);
        } else {
            log.trace(msg);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (laLog != null) {
            log(LocationAwareLogger.TRACE_INT, MessageFormatter.arrayFormat(format, arguments).getMessage());
        } else {
            log.trace(format, arguments);
        }
    }

    @Override
    public void trace(Object obj) {
        trace(ObjectUtils.toString(obj));
    }

    @Override
    public void trace(Throwable e) {
        if (laLog != null) {
            log(LocationAwareLogger.TRACE_INT, e);
        } else {
            log.trace(e.getMessage(), e);
        }
    }

    @Override
    public void trace(String msg, Throwable e) {
        if (laLog != null) {
            log(LocationAwareLogger.TRACE_INT, msg, e);
        } else {
            log.trace(msg, e);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return ObjectUtils.defaultIfNull(laLog, org.slf4j.Logger::isDebugEnabled, log.isDebugEnabled());
    }

    @Override
    public void debug(String msg) {
        if (laLog != null) {
            log(LocationAwareLogger.DEBUG_INT, msg);
        } else {
            log.debug(msg);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (laLog != null) {
            log(LocationAwareLogger.DEBUG_INT, MessageFormatter.arrayFormat(format, arguments).getMessage());
        } else {
            log.debug(format, arguments);
        }
    }

    @Override
    public void debug(Object obj) {
        debug(ObjectUtils.toString(obj));
    }

    @Override
    public void debug(Throwable e) {
        if (laLog != null) {
            log(LocationAwareLogger.DEBUG_INT, e);
        } else {
            log.debug(e.getMessage(), e);
        }
    }

    @Override
    public void debug(String msg, Throwable e) {
        if (laLog != null) {
            log(LocationAwareLogger.DEBUG_INT, msg, e);
        } else {
            log.debug(msg, e);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return ObjectUtils.defaultIfNull(laLog, org.slf4j.Logger::isInfoEnabled, log.isInfoEnabled());
    }

    @Override
    public void info(String msg) {
        if (laLog != null) {
            log(LocationAwareLogger.INFO_INT, msg);
        } else {
            log.info(msg);
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (laLog != null) {
            log(LocationAwareLogger.INFO_INT, MessageFormatter.arrayFormat(format, arguments).getMessage());
        } else {
            log.info(format, arguments);
        }
    }

    @Override
    public void info(Object obj) {
        info(ObjectUtils.toString(obj));
    }

    @Override
    public void info(Throwable e) {
        if (laLog != null) {
            log(LocationAwareLogger.INFO_INT, e);
        } else {
            log.info(e.getMessage(), e);
        }
    }

    @Override
    public void info(String msg, Throwable e) {
        if (laLog != null) {
            log(LocationAwareLogger.INFO_INT, msg, e);
        } else {
            log.info(msg, e);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return ObjectUtils.defaultIfNull(laLog, org.slf4j.Logger::isWarnEnabled, log.isWarnEnabled());
    }

    @Override
    public void warn(String msg) {
        if (laLog != null) {
            log(LocationAwareLogger.WARN_INT, msg);
        } else {
            log.warn(msg);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (laLog != null) {
            log(LocationAwareLogger.WARN_INT, MessageFormatter.arrayFormat(format, arguments).getMessage());
        } else {
            log.warn(format, arguments);
        }
    }

    @Override
    public void warn(Object obj) {
        warn(ObjectUtils.toString(obj));
    }

    @Override
    public void warn(Throwable e) {
        if (laLog != null) {
            log(LocationAwareLogger.WARN_INT, e);
        } else {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void warn(String msg, Throwable e) {
        if (laLog != null) {
            log(LocationAwareLogger.WARN_INT, msg, e);
        } else {
            log.warn(msg, e);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return ObjectUtils.defaultIfNull(laLog, org.slf4j.Logger::isErrorEnabled, log.isErrorEnabled());
    }

    @Override
    public void error(String msg) {
        if (laLog != null) {
            log(LocationAwareLogger.ERROR_INT, msg);
        } else {
            log.error(msg);
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (laLog != null) {
            log(LocationAwareLogger.ERROR_INT, MessageFormatter.arrayFormat(format, arguments).getMessage());
        } else {
            log.error(format, arguments);
        }
    }

    @Override
    public void error(Object obj) {
        error(ObjectUtils.toString(obj));
    }

    @Override
    public void error(Throwable e) {
        if (laLog != null) {
            log(LocationAwareLogger.ERROR_INT, e);
        } else {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void error(String msg, Throwable e) {
        if (laLog != null) {
            log(LocationAwareLogger.ERROR_INT, msg, e);
        } else {
            log.error(msg, e);
        }
    }

    private void log(int i, String msg) {
        log(i, msg, null);
    }

    private void log(int i, Throwable e) {
        log(i, e.getMessage(), e);
    }

    private void log(int i, String msg, Throwable e) {
        laLog.log(null, log.getName(), i, msg, null, e);
    }
}