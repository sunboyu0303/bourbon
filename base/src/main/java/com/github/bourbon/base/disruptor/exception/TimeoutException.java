package com.github.bourbon.base.disruptor.exception;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:33
 */
public final class TimeoutException extends Exception {

    public static final TimeoutException INSTANCE = new TimeoutException();

    private TimeoutException() {
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}