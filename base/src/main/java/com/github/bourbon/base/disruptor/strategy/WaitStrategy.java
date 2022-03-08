package com.github.bourbon.base.disruptor.strategy;

import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.exception.AlertException;
import com.github.bourbon.base.disruptor.exception.TimeoutException;
import com.github.bourbon.base.disruptor.lang.Sequence;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 18:48
 */
public interface WaitStrategy {

    long waitFor(long sequence, Sequence cursor, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException, InterruptedException, TimeoutException;

    default void signalAllWhenBlocking() {
    }
}