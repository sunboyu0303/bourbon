package com.github.bourbon.base.logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 21:49
 */
class FailSafeLogger implements Logger {

    private static final Logger LOG = new SystemLogger();

    private static final AtomicBoolean DISABLED = new AtomicBoolean(false);

    public static void setDisabled(boolean disabled) {
        DISABLED.set(disabled);
    }

    private final Logger logger;

    FailSafeLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isTraceEnabled() {
        try {
            return !DISABLED.get() && logger.isTraceEnabled();
        } catch (Exception t) {
            return false;
        }
    }

    @Override
    public void trace(String msg) {
        try {
            if (!isTraceEnabled()) {
                return;
            }
            logger.trace(msg);
        } catch (Exception t) {
            LOG.trace(msg);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        try {
            if (!isTraceEnabled()) {
                return;
            }
            logger.trace(format, arguments);
        } catch (Exception t) {
            LOG.trace(format, arguments);
        }
    }

    @Override
    public void trace(Object obj) {
        try {
            if (!isTraceEnabled()) {
                return;
            }
            logger.trace(obj);
        } catch (Exception t) {
            LOG.trace(obj);
        }
    }

    @Override
    public void trace(Throwable e) {
        try {
            if (!isTraceEnabled()) {
                return;
            }
            logger.trace(e);
        } catch (Exception t) {
            LOG.trace(e);
        }
    }

    @Override
    public void trace(String msg, Throwable e) {
        try {
            if (!isTraceEnabled()) {
                return;
            }
            logger.trace(msg, e);
        } catch (Exception t) {
            LOG.trace(msg, e);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        try {
            return !DISABLED.get() && logger.isDebugEnabled();
        } catch (Exception t) {
            return false;
        }
    }

    @Override
    public void debug(String msg) {
        try {
            if (!isDebugEnabled()) {
                return;
            }
            logger.debug(msg);
        } catch (Exception t) {
            LOG.debug(msg);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        try {
            if (!isDebugEnabled()) {
                return;
            }
            logger.debug(format, arguments);
        } catch (Exception t) {
            LOG.debug(format, arguments);
        }
    }

    @Override
    public void debug(Object obj) {
        try {
            if (!isDebugEnabled()) {
                return;
            }
            logger.debug(obj);
        } catch (Exception t) {
            LOG.debug(obj);
        }
    }

    @Override
    public void debug(Throwable e) {
        try {
            if (!isDebugEnabled()) {
                return;
            }
            logger.debug(e);
        } catch (Exception t) {
            LOG.debug(e);
        }
    }

    @Override
    public void debug(String msg, Throwable e) {
        try {
            if (!isDebugEnabled()) {
                return;
            }
            logger.debug(msg, e);
        } catch (Exception t) {
            LOG.debug(msg, e);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        try {
            return !DISABLED.get() && logger.isInfoEnabled();
        } catch (Exception t) {
            return false;
        }
    }

    @Override
    public void info(String msg) {
        try {
            if (!isInfoEnabled()) {
                return;
            }
            logger.info(msg);
        } catch (Exception t) {
            LOG.info(msg);
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        try {
            if (!isInfoEnabled()) {
                return;
            }
            logger.info(format, arguments);
        } catch (Exception t) {
            LOG.info(format, arguments);
        }
    }

    @Override
    public void info(Object obj) {
        try {
            if (!isInfoEnabled()) {
                return;
            }
            logger.info(obj);
        } catch (Exception t) {
            LOG.info(obj);
        }
    }

    @Override
    public void info(Throwable e) {
        try {
            if (!isInfoEnabled()) {
                return;
            }
            logger.info(e);
        } catch (Exception t) {
            LOG.info(e);
        }
    }

    @Override
    public void info(String msg, Throwable e) {
        try {
            if (!isInfoEnabled()) {
                return;
            }
            logger.info(msg, e);
        } catch (Exception t) {
            LOG.info(msg, e);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        try {
            return !DISABLED.get() && logger.isWarnEnabled();
        } catch (Exception t) {
            return false;
        }
    }

    @Override
    public void warn(String msg) {
        try {
            if (!isWarnEnabled()) {
                return;
            }
            logger.warn(msg);
        } catch (Exception t) {
            LOG.warn(msg);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        try {
            if (!isWarnEnabled()) {
                return;
            }
            logger.warn(format, arguments);
        } catch (Exception t) {
            LOG.warn(format, arguments);
        }
    }

    @Override
    public void warn(Object obj) {
        try {
            if (!isWarnEnabled()) {
                return;
            }
            logger.warn(obj);
        } catch (Exception t) {
            LOG.warn(obj);
        }
    }

    @Override
    public void warn(Throwable e) {
        try {
            if (!isWarnEnabled()) {
                return;
            }
            logger.warn(e);
        } catch (Exception t) {
            LOG.warn(e);
        }
    }

    @Override
    public void warn(String msg, Throwable e) {
        try {
            if (!isWarnEnabled()) {
                return;
            }
            logger.warn(msg, e);
        } catch (Exception t) {
            LOG.warn(msg, e);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        try {
            return !DISABLED.get() && logger.isErrorEnabled();
        } catch (Exception t) {
            return false;
        }
    }

    @Override
    public void error(String msg) {
        try {
            if (!isErrorEnabled()) {
                return;
            }
            logger.error(msg);
        } catch (Exception t) {
            LOG.error(msg);
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        try {
            if (!isErrorEnabled()) {
                return;
            }
            logger.error(format, arguments);
        } catch (Exception t) {
            LOG.error(format, arguments);
        }
    }

    @Override
    public void error(Object obj) {
        try {
            if (!isErrorEnabled()) {
                return;
            }
            logger.error(obj);
        } catch (Exception t) {
            LOG.error(obj);
        }
    }

    @Override
    public void error(Throwable e) {
        try {
            if (!isErrorEnabled()) {
                return;
            }
            logger.error(e);
        } catch (Exception t) {
            LOG.error(e);
        }
    }

    @Override
    public void error(String msg, Throwable e) {
        try {
            if (!isErrorEnabled()) {
                return;
            }
            logger.error(msg, e);
        } catch (Exception t) {
            LOG.error(msg, e);
        }
    }
}