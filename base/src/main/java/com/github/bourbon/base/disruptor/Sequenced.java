package com.github.bourbon.base.disruptor;

import com.github.bourbon.base.disruptor.exception.InsufficientCapacityException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 19:38
 */
public interface Sequenced {

    int getBufferSize();

    boolean hasAvailableCapacity(int requiredCapacity);

    long remainingCapacity();

    long next();

    long next(int n);

    long tryNext() throws InsufficientCapacityException;

    long tryNext(int n) throws InsufficientCapacityException;

    void publish(long sequence);

    void publish(long lo, long hi);
}