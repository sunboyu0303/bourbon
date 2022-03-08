package com.github.bourbon.base.disruptor.processor;

import com.github.bourbon.base.disruptor.EventReleaseAware;
import com.github.bourbon.base.disruptor.EventReleaser;
import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.Sequencer;
import com.github.bourbon.base.disruptor.exception.AlertException;
import com.github.bourbon.base.disruptor.exception.TimeoutException;
import com.github.bourbon.base.disruptor.handler.ExceptionHandler;
import com.github.bourbon.base.disruptor.handler.TimeoutHandler;
import com.github.bourbon.base.disruptor.handler.WorkHandler;
import com.github.bourbon.base.disruptor.lang.RingBuffer;
import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.extension.Lifecycle;
import com.github.bourbon.base.utils.BooleanUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 16:43
 */
public final class WorkProcessor<T> implements EventProcessor {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Sequence sequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
    private final RingBuffer<T> ringBuffer;
    private final SequenceBarrier sequenceBarrier;
    private final WorkHandler<? super T> workHandler;
    private final ExceptionHandler<? super T> exceptionHandler;
    private final Sequence workSequence;
    private final EventReleaser eventReleaser = () -> sequence.set(Long.MAX_VALUE);
    private final TimeoutHandler timeoutHandler;

    public WorkProcessor(RingBuffer<T> ringBuffer, SequenceBarrier sequenceBarrier, WorkHandler<? super T> workHandler, ExceptionHandler<? super T> exceptionHandler, Sequence workSequence) {
        this.ringBuffer = ringBuffer;
        this.sequenceBarrier = sequenceBarrier;
        this.workHandler = workHandler;
        this.exceptionHandler = exceptionHandler;
        this.workSequence = workSequence;
        if (this.workHandler instanceof EventReleaseAware) {
            ((EventReleaseAware) this.workHandler).setEventReleaser(eventReleaser);
        }
        timeoutHandler = BooleanUtils.defaultIfAssignableFrom(workHandler, TimeoutHandler.class, TimeoutHandler.class::cast);
    }

    @Override
    public Sequence getSequence() {
        return sequence;
    }

    @Override
    public void halt() {
        running.set(false);
        sequenceBarrier.alert();
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
        sequenceBarrier.clearAlert();

        notifyStart();
        boolean processedSequence = true;
        long cachedAvailableSequence = Long.MIN_VALUE;
        long nextSequence = sequence.get();
        T event = null;
        while (true) {
            try {
                if (processedSequence) {
                    processedSequence = false;
                    do {
                        nextSequence = workSequence.get() + 1L;
                        sequence.set(nextSequence - 1L);
                    } while (!workSequence.compareAndSet(nextSequence - 1L, nextSequence));
                }
                if (cachedAvailableSequence >= nextSequence) {
                    event = ringBuffer.get(nextSequence);
                    workHandler.onEvent(event);
                    processedSequence = true;
                } else {
                    cachedAvailableSequence = sequenceBarrier.waitFor(nextSequence);
                }
            } catch (TimeoutException e) {
                notifyTimeout(sequence.get());
            } catch (AlertException ex) {
                if (!running.get()) {
                    break;
                }
            } catch (Throwable ex) {
                exceptionHandler.handleEventException(ex, nextSequence, event);
                processedSequence = true;
            }
        }

        notifyShutdown();

        running.set(false);
    }

    private void notifyTimeout(final long availableSequence) {
        try {
            if (timeoutHandler != null) {
                timeoutHandler.onTimeout(availableSequence);
            }
        } catch (Throwable e) {
            exceptionHandler.handleEventException(e, availableSequence, null);
        }
    }

    private void notifyStart() {
        if (workHandler instanceof Lifecycle) {
            try {
                ((Lifecycle) workHandler).initialize();
            } catch (Throwable ex) {
                exceptionHandler.handleOnStartException(ex);
            }
        }
    }

    private void notifyShutdown() {
        if (workHandler instanceof Lifecycle) {
            try {
                ((Lifecycle) workHandler).destroy();
            } catch (Throwable ex) {
                exceptionHandler.handleOnShutdownException(ex);
            }
        }
    }
}