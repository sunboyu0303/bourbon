package com.github.bourbon.base.disruptor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:42
 */
public interface DataProvider<T> {

    T get(long sequence);
}