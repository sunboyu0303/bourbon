package com.github.bourbon.base.disruptor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:43
 */
public interface EventFactory<T> {

    T newInstance();
}