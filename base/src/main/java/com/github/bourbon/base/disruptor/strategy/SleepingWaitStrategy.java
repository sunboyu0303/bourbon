package com.github.bourbon.base.disruptor.strategy;

import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.exception.AlertException;
import com.github.bourbon.base.disruptor.lang.Sequence;

import java.util.concurrent.locks.LockSupport;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 10:00
 */
public final class SleepingWaitStrategy implements WaitStrategy {

    private final int retries;

    public SleepingWaitStrategy() {
        this(200);
    }

    public SleepingWaitStrategy(int retries) {
        this.retries = retries;
    }

    @Override
    public long waitFor(long sequence, Sequence cursor, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException {
        long availableSequence;
        int counter = retries;
        while ((availableSequence = dependentSequence.get()) < sequence) {
            counter = applyWaitMethod(barrier, counter);
        }
        return availableSequence;
    }

    private int applyWaitMethod(SequenceBarrier barrier, int counter) throws AlertException {
        barrier.checkAlert();
        if (counter > 100) {
            --counter;
        } else if (counter > 0) {
            --counter;
            Thread.yield();
        } else {
            LockSupport.parkNanos(1L);
        }
        return counter;
    }
}