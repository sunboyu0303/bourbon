package com.github.bourbon.base.disruptor.handler;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 11:27
 */
public final class IgnoreExceptionHandler implements ExceptionHandler<Object> {
    private final Logger logger;

    public IgnoreExceptionHandler() {
        this(LoggerFactory.getLogger(IgnoreExceptionHandler.class));
    }

    public IgnoreExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handleEventException(final Throwable ex, final long sequence, final Object event) {
        logger.info("Exception processing: {} {}", sequence, event, ex);
    }

    @Override
    public void handleOnStartException(final Throwable ex) {
        logger.info("Exception during onStart.", ex);
    }

    @Override
    public void handleOnShutdownException(final Throwable ex) {
        logger.info("Exception during onShutdown.", ex);
    }
}