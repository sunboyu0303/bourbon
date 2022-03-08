package com.github.bourbon.base.disruptor.handler;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/31 20:32
 */
public interface WorkHandler<T> {
    
    void onEvent(T event) throws Exception;
}