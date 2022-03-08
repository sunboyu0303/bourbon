package com.github.bourbon.base.disruptor;

import com.github.bourbon.base.disruptor.lang.Sequence;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 17:06
 */
public class EventPoller<T> {

    private final DataProvider<T> dataProvider;
    private final Sequencer sequencer;
    private final Sequence sequence;
    private final Sequence gatingSequence;

    public interface Handler<T> {
        boolean onEvent(T event, long sequence, boolean endOfBatch) throws Exception;
    }

    public enum PollState {
        PROCESSING, GATING, IDLE
    }

    public EventPoller(DataProvider<T> dataProvider, Sequencer sequencer, Sequence sequence, Sequence gatingSequence) {
        this.dataProvider = dataProvider;
        this.sequencer = sequencer;
        this.sequence = sequence;
        this.gatingSequence = gatingSequence;
    }

    public PollState poll(Handler<T> eventHandler) throws Exception {
        long currentSequence = sequence.get();
        long nextSequence = currentSequence + 1;
        long availableSequence = sequencer.getHighestPublishedSequence(nextSequence, gatingSequence.get());

        if (nextSequence <= availableSequence) {
            boolean processNextEvent;
            long processedSequence = currentSequence;
            try {
                do {
                    final T event = dataProvider.get(nextSequence);
                    processNextEvent = eventHandler.onEvent(event, nextSequence, nextSequence == availableSequence);
                    processedSequence = nextSequence;
                    nextSequence++;

                } while (nextSequence <= availableSequence && processNextEvent);
            } finally {
                sequence.set(processedSequence);
            }
            return PollState.PROCESSING;
        } else if (sequencer.getCursor() >= nextSequence) {
            return PollState.GATING;
        } else {
            return PollState.IDLE;
        }
    }

    public static <T> EventPoller<T> newInstance(final DataProvider<T> dataProvider, final Sequencer sequencer, final Sequence sequence, final Sequence cursorSequence, final Sequence... gatingSequences) {
        Sequence gatingSequence;
        if (gatingSequences.length == 0) {
            gatingSequence = cursorSequence;
        } else if (gatingSequences.length == 1) {
            gatingSequence = gatingSequences[0];
        } else {
            gatingSequence = new FixedSequenceGroup(gatingSequences);
        }
        return new EventPoller<>(dataProvider, sequencer, sequence, gatingSequence);
    }

    public Sequence getSequence() {
        return sequence;
    }
}