package com.github.bourbon.base.disruptor.strategy;

import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.exception.AlertException;
import com.github.bourbon.base.disruptor.exception.TimeoutException;
import com.github.bourbon.base.disruptor.lang.Sequence;

import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 12:04
 */
public final class PhasedBackoffWaitStrategy implements WaitStrategy {

    private static final int SPIN_TRIES = 10000;
    private final long spinTimeoutNanos;
    private final long yieldTimeoutNanos;
    private final WaitStrategy fallbackStrategy;

    private PhasedBackoffWaitStrategy(long spinTimeout, long yieldTimeout, TimeUnit units, WaitStrategy fallbackStrategy) {
        this.spinTimeoutNanos = units.toNanos(spinTimeout);
        this.yieldTimeoutNanos = spinTimeoutNanos + units.toNanos(yieldTimeout);
        this.fallbackStrategy = fallbackStrategy;
    }

    public static PhasedBackoffWaitStrategy withLock(long spinTimeout, long yieldTimeout, TimeUnit units) {
        return new PhasedBackoffWaitStrategy(spinTimeout, yieldTimeout, units, new BlockingWaitStrategy());
    }

    public static PhasedBackoffWaitStrategy withLiteLock(long spinTimeout, long yieldTimeout, TimeUnit units) {
        return new PhasedBackoffWaitStrategy(spinTimeout, yieldTimeout, units, new LiteBlockingWaitStrategy());
    }

    public static PhasedBackoffWaitStrategy withSleep(long spinTimeout, long yieldTimeout, TimeUnit units) {
        return new PhasedBackoffWaitStrategy(spinTimeout, yieldTimeout, units, new SleepingWaitStrategy(0));
    }

    @Override
    public long waitFor(long sequence, Sequence cursor, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException, InterruptedException, TimeoutException {
        long availableSequence;
        long startTime = 0;
        int counter = SPIN_TRIES;
        do {
            if ((availableSequence = dependentSequence.get()) >= sequence) {
                return availableSequence;
            }
            if (0 == --counter) {
                if (0 == startTime) {
                    startTime = System.nanoTime();
                } else {
                    long timeDelta = System.nanoTime() - startTime;
                    if (timeDelta > yieldTimeoutNanos) {
                        return fallbackStrategy.waitFor(sequence, cursor, dependentSequence, barrier);
                    } else if (timeDelta > spinTimeoutNanos) {
                        Thread.yield();
                    }
                }
                counter = SPIN_TRIES;
            }
        } while (true);
    }

    @Override
    public void signalAllWhenBlocking() {
        fallbackStrategy.signalAllWhenBlocking();
    }
}