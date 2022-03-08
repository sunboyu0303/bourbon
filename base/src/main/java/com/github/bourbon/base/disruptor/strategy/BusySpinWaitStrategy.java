package com.github.bourbon.base.disruptor.strategy;

import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.exception.AlertException;
import com.github.bourbon.base.disruptor.lang.Sequence;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 10:20
 */
public final class BusySpinWaitStrategy implements WaitStrategy {
    
    @Override
    public long waitFor(long sequence, Sequence cursor, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException {
        long availableSequence;
        while ((availableSequence = dependentSequence.get()) < sequence) {
            barrier.checkAlert();
        }
        return availableSequence;
    }
}