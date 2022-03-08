package com.github.bourbon.base.disruptor.processor;

import com.github.bourbon.base.disruptor.Sequencer;
import com.github.bourbon.base.disruptor.lang.RingBuffer;
import com.github.bourbon.base.disruptor.lang.Sequence;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 11:52
 */
public final class NoOpEventProcessor implements EventProcessor {
    private final SequencerFollowingSequence sequence;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public NoOpEventProcessor(final RingBuffer<?> sequencer) {
        sequence = new SequencerFollowingSequence(sequencer);
    }

    @Override
    public Sequence getSequence() {
        return sequence;
    }

    @Override
    public void halt() {
        running.set(false);
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        if (!running.compareAndSet(false, true)) {
            throw new IllegalStateException("Thread is already running");
        }
    }

    private static final class SequencerFollowingSequence extends Sequence {
        private final RingBuffer<?> sequencer;

        private SequencerFollowingSequence(final RingBuffer<?> sequencer) {
            super(Sequencer.INITIAL_CURSOR_VALUE);
            this.sequencer = sequencer;
        }

        @Override
        public long get() {
            return sequencer.getCursor();
        }
    }
}