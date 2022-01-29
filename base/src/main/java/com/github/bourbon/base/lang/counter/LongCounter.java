package com.github.bourbon.base.lang.counter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 11:10
 */
public interface LongCounter {

    void add(long l);

    void increment();

    void decrement();

    long value();
}