package com.github.bourbon.base.disruptor.strategy;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.exception.AlertException;
import com.github.bourbon.base.disruptor.lang.Sequence;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 11:31
 */
public final class LiteBlockingWaitStrategy implements WaitStrategy {

    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();
    private final AtomicBoolean signalNeeded = new AtomicBoolean(false);

    @Override
    public long waitFor(long sequence, Sequence cursorSequence, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException, InterruptedException {
        long availableSequence;
        if (cursorSequence.get() < sequence) {
            lock.lock();
            try {
                do {
                    signalNeeded.getAndSet(true);
                    if (cursorSequence.get() >= sequence) {
                        break;
                    }
                    barrier.checkAlert();
                    processorNotifyCondition.await();
                } while (cursorSequence.get() < sequence);
            } finally {
                lock.unlock();
            }
        }
        while ((availableSequence = dependentSequence.get()) < sequence) {
            barrier.checkAlert();
        }
        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking() {
        if (signalNeeded.getAndSet(false)) {
            lock.lock();
            try {
                processorNotifyCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public String toString() {
        return "LiteBlockingWaitStrategy{processorNotifyCondition=" + processorNotifyCondition + CharConstants.RIGHT_BRACES;
    }
}