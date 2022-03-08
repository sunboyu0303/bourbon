package com.github.bourbon.base.disruptor.handler;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:42
 */
public interface TimeoutHandler {

    void onTimeout(long sequence) throws Exception;
}