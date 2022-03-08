package com.github.bourbon.base.disruptor;

import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.utils.Utils;

import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 11:22
 */
public final class FixedSequenceGroup extends Sequence {
    private final Sequence[] sequences;

    public FixedSequenceGroup(Sequence[] sequences) {
        this.sequences = Arrays.copyOf(sequences, sequences.length);
    }

    @Override
    public long get() {
        return Utils.getMinimumSequence(sequences);
    }

    @Override
    public String toString() {
        return Arrays.toString(sequences);
    }

    @Override
    public void set(long value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean compareAndSet(long expectedValue, long newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long incrementAndGet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long addAndGet(long increment) {
        throw new UnsupportedOperationException();
    }
}