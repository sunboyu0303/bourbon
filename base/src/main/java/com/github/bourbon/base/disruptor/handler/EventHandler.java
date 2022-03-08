package com.github.bourbon.base.disruptor.handler;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:48
 */
public interface EventHandler<T> {

    void onEvent(T event, long sequence, boolean endOfBatch) throws Exception;
}