package com.github.bourbon.base.disruptor.dsl;

import com.github.bourbon.base.disruptor.handler.ExceptionHandler;
import com.github.bourbon.base.disruptor.handler.FatalExceptionHandler;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 22:47
 */
public class ExceptionHandlerWrapper<T> implements ExceptionHandler<T> {
    private ExceptionHandler<? super T> delegate = new FatalExceptionHandler();

    public void switchTo(ExceptionHandler<? super T> exceptionHandler) {
        this.delegate = exceptionHandler;
    }

    @Override
    public void handleEventException(Throwable ex, long sequence, T event) {
        delegate.handleEventException(ex, sequence, event);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        delegate.handleOnStartException(ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        delegate.handleOnShutdownException(ex);
    }
}