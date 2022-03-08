package com.github.bourbon.base.disruptor.lang;

import com.github.bourbon.base.disruptor.exception.InsufficientCapacityException;
import com.github.bourbon.base.disruptor.strategy.WaitStrategy;
import com.github.bourbon.base.utils.Utils;

import java.util.concurrent.locks.LockSupport;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 16:40
 */
public final class SingleProducerSequencer extends SingleProducerSequencerFields {

    protected long p1, p2, p3, p4, p5, p6, p7;

    public SingleProducerSequencer(int bufferSize, final WaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
    }

    @Override
    public boolean hasAvailableCapacity(final int requiredCapacity) {
        long nextValue = this.nextValue;
        long wrapPoint = (nextValue + requiredCapacity) - bufferSize;
        long cachedGatingSequence = this.cachedValue;

        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > nextValue) {
            cursor.setVolatile(nextValue);
            long minSequence = Utils.getMinimumSequence(gatingSequences, nextValue);
            this.cachedValue = minSequence;
            if (wrapPoint > minSequence) {
                return false;
            }
        }

        return true;
    }

    @Override
    public long next() {
        return next(1);
    }

    @Override
    public long next(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }

        long nextValue = this.nextValue;
        long nextSequence = nextValue + n;
        long wrapPoint = nextSequence - bufferSize;
        long cachedGatingSequence = this.cachedValue;

        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > nextValue) {
            cursor.setVolatile(nextValue);
            long minSequence;
            while (wrapPoint > (minSequence = Utils.getMinimumSequence(gatingSequences, nextValue))) {
                waitStrategy.signalAllWhenBlocking();
                LockSupport.parkNanos(1L);
            }
            this.cachedValue = minSequence;
        }

        this.nextValue = nextSequence;

        return nextSequence;
    }

    @Override
    public long tryNext() throws InsufficientCapacityException {
        return tryNext(1);
    }

    @Override
    public long tryNext(int n) throws InsufficientCapacityException {
        if (n < 1) {
            throw new IllegalArgumentException("n must be > 0");
        }
        if (!hasAvailableCapacity(n)) {
            throw InsufficientCapacityException.INSTANCE;
        }
        return this.nextValue += n;
    }

    @Override
    public long remainingCapacity() {
        long nextValue = this.nextValue;
        long consumed = Utils.getMinimumSequence(gatingSequences, nextValue);
        long produced = nextValue;
        return getBufferSize() - (produced - consumed);
    }

    @Override
    public void claim(long sequence) {
        this.nextValue = sequence;
    }

    @Override
    public void publish(long sequence) {
        cursor.set(sequence);
        waitStrategy.signalAllWhenBlocking();
    }

    @Override
    public void publish(long lo, long hi) {
        publish(hi);
    }

    @Override
    public boolean isAvailable(long sequence) {
        return sequence <= cursor.get();
    }

    @Override
    public long getHighestPublishedSequence(long lowerBound, long availableSequence) {
        return availableSequence;
    }
}