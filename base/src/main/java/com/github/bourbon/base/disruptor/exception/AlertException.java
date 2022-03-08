package com.github.bourbon.base.disruptor.exception;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:31
 */
public final class AlertException extends Exception {

    public static final AlertException INSTANCE = new AlertException();

    private AlertException() {
    }

    /**
     * Overridden so the stack trace is not filled in for this exception for performance reasons.
     *
     * @return this instance.
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}