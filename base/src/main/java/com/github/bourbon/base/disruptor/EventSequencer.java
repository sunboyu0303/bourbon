package com.github.bourbon.base.disruptor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 10:55
 */
public interface EventSequencer<T> extends DataProvider<T>, Sequenced {
}