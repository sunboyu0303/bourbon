package com.github.bourbon.base.disruptor.handler;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:49
 */
public interface ExceptionHandler<T> {

    void handleEventException(Throwable ex, long sequence, T event);

    void handleOnStartException(Throwable ex);

    void handleOnShutdownException(Throwable ex);
}