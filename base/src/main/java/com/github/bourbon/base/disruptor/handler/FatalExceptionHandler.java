package com.github.bourbon.base.disruptor.handler;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 20:01
 */
public final class FatalExceptionHandler implements ExceptionHandler<Object> {

    private final Logger logger;

    public FatalExceptionHandler() {
        this(LoggerFactory.getLogger(FatalExceptionHandler.class));
    }

    public FatalExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        logger.error("Exception processing: {} {}", sequence, event, ex);
        throw new RuntimeException(ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        logger.error("Exception during onStart.", ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        logger.error("Exception during onShutdown.", ex);
    }
}