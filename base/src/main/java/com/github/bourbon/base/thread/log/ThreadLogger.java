package com.github.bourbon.base.thread.log;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 15:32
 */
public final class ThreadLogger {

    private static final Logger THREAD_LOGGER = LoggerFactory.getLogger(ThreadLogger.class);

    public static void debug(String format, Object... arguments) {
        if (THREAD_LOGGER.isDebugEnabled()) {
            THREAD_LOGGER.debug(format, arguments);
        }
    }

    public static void info(String format, Object... arguments) {
        if (THREAD_LOGGER.isInfoEnabled()) {
            THREAD_LOGGER.info(format, arguments);
        }
    }

    public static void warn(String format, Object... arguments) {
        if (THREAD_LOGGER.isWarnEnabled()) {
            THREAD_LOGGER.warn(format, arguments);
        }
    }

    public static void error(String format, Object... arguments) {
        if (THREAD_LOGGER.isErrorEnabled()) {
            THREAD_LOGGER.error(format, arguments);
        }
    }

    private ThreadLogger() {
    }
}