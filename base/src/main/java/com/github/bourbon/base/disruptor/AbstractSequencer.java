package com.github.bourbon.base.disruptor;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.disruptor.strategy.WaitStrategy;
import com.github.bourbon.base.utils.ArrayUtils;
import com.github.bourbon.base.utils.Utils;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 11:38
 */
public abstract class AbstractSequencer implements Sequencer {
    private static final AtomicReferenceFieldUpdater<AbstractSequencer, Sequence[]> SEQUENCE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(AbstractSequencer.class, Sequence[].class, "gatingSequences");
    protected final int bufferSize;
    protected final WaitStrategy waitStrategy;
    protected final Sequence cursor = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
    protected volatile Sequence[] gatingSequences = new Sequence[0];

    public AbstractSequencer(int bufferSize, WaitStrategy waitStrategy) {
        if (bufferSize < 1) {
            throw new IllegalArgumentException("bufferSize must not be less than 1");
        }
        if (Integer.bitCount(bufferSize) != 1) {
            throw new IllegalArgumentException("bufferSize must be a power of 2");
        }
        this.bufferSize = bufferSize;
        this.waitStrategy = waitStrategy;
    }

    @Override
    public final long getCursor() {
        return cursor.get();
    }

    @Override
    public final int getBufferSize() {
        return bufferSize;
    }

    @Override
    public final void addGatingSequences(Sequence... gatingSequences) {
        SequenceGroups.addSequences(this, SEQUENCE_UPDATER, this, gatingSequences);
    }

    @Override
    public boolean removeGatingSequence(Sequence sequence) {
        return SequenceGroups.removeSequence(this, SEQUENCE_UPDATER, sequence);
    }

    @Override
    public long getMinimumSequence() {
        return Utils.getMinimumSequence(gatingSequences, cursor.get());
    }

    @Override
    public SequenceBarrier newBarrier(Sequence... sequencesToTrack) {
        return new ProcessingSequenceBarrier(this, waitStrategy, cursor, sequencesToTrack);
    }

    @Override
    public <T> EventPoller<T> newPoller(DataProvider<T> dataProvider, Sequence... gatingSequences) {
        return EventPoller.newInstance(dataProvider, this, new Sequence(), cursor, gatingSequences);
    }

    @Override
    public String toString() {
        return "AbstractSequencer{waitStrategy=" + waitStrategy + ", cursor=" + cursor + ", gatingSequences=" + ArrayUtils.toString(gatingSequences) + CharConstants.RIGHT_BRACES;
    }
}