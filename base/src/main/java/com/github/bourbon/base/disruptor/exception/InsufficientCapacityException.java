package com.github.bourbon.base.disruptor.exception;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:44
 */
public final class InsufficientCapacityException extends Exception {

    public static final InsufficientCapacityException INSTANCE = new InsufficientCapacityException();

    private InsufficientCapacityException() {
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}