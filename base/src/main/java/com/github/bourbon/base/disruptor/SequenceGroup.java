package com.github.bourbon.base.disruptor;

import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.utils.Utils;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 16:27
 */
public final class SequenceGroup extends Sequence {
    private static final AtomicReferenceFieldUpdater<SequenceGroup, Sequence[]> SEQUENCE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(SequenceGroup.class, Sequence[].class, "sequences");
    private volatile Sequence[] sequences = new Sequence[0];

    public SequenceGroup() {
        super(-1);
    }

    @Override
    public long get() {
        return Utils.getMinimumSequence(sequences);
    }

    @Override
    public void set(long value) {
        for (Sequence sequence : this.sequences) {
            sequence.set(value);
        }
    }

    public void add(Sequence sequence) {
        Sequence[] oldSequences;
        Sequence[] newSequences;
        do {
            oldSequences = sequences;
            final int oldSize = oldSequences.length;
            newSequences = new Sequence[oldSize + 1];
            System.arraycopy(oldSequences, 0, newSequences, 0, oldSize);
            newSequences[oldSize] = sequence;
        } while (!SEQUENCE_UPDATER.compareAndSet(this, oldSequences, newSequences));
    }

    public boolean remove(Sequence sequence) {
        return SequenceGroups.removeSequence(this, SEQUENCE_UPDATER, sequence);
    }

    public int size() {
        return sequences.length;
    }

    public void addWhileRunning(Cursored cursored, Sequence sequence) {
        SequenceGroups.addSequences(this, SEQUENCE_UPDATER, cursored, sequence);
    }
}