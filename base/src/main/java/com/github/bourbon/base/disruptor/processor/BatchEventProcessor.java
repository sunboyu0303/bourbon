package com.github.bourbon.base.disruptor.processor;

import com.github.bourbon.base.disruptor.DataProvider;
import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.Sequencer;
import com.github.bourbon.base.disruptor.exception.AlertException;
import com.github.bourbon.base.disruptor.exception.TimeoutException;
import com.github.bourbon.base.disruptor.handler.*;
import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.extension.Lifecycle;
import com.github.bourbon.base.utils.BooleanUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 22:51
 */
public final class BatchEventProcessor<T> implements EventProcessor {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private ExceptionHandler<? super T> exceptionHandler = new FatalExceptionHandler();
    private final DataProvider<T> dataProvider;
    private final SequenceBarrier sequenceBarrier;
    private final EventHandler<? super T> eventHandler;
    private final Sequence sequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
    private final TimeoutHandler timeoutHandler;

    public BatchEventProcessor(DataProvider<T> dataProvider, SequenceBarrier sequenceBarrier, EventHandler<? super T> eventHandler) {
        this.dataProvider = dataProvider;
        this.sequenceBarrier = sequenceBarrier;
        this.eventHandler = eventHandler;

        if (eventHandler instanceof SequenceReportingEventHandler) {
            ((SequenceReportingEventHandler<?>) eventHandler).setSequenceCallback(sequence);
        }

        timeoutHandler = BooleanUtils.defaultIfAssignableFrom(eventHandler, TimeoutHandler.class, TimeoutHandler.class::cast);
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

    public void setExceptionHandler(final ExceptionHandler<? super T> exceptionHandler) {
        if (null == exceptionHandler) {
            throw new NullPointerException();
        }
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void run() {
        if (!running.compareAndSet(false, true)) {
            throw new IllegalStateException("Thread is already running");
        }
        sequenceBarrier.clearAlert();

        notifyStart();

        T event = null;
        long nextSequence = sequence.get() + 1L;
        try {
            while (true) {
                try {
                    long availableSequence = sequenceBarrier.waitFor(nextSequence);
                    while (nextSequence <= availableSequence) {
                        event = dataProvider.get(nextSequence);
                        eventHandler.onEvent(event, nextSequence, nextSequence == availableSequence);
                        nextSequence++;
                    }
                    sequence.set(availableSequence);
                } catch (TimeoutException e) {
                    notifyTimeout(sequence.get());
                } catch (AlertException ex) {
                    if (!running.get()) {
                        break;
                    }
                } catch (Throwable ex) {
                    exceptionHandler.handleEventException(ex, nextSequence, event);
                    sequence.set(nextSequence);
                    nextSequence++;
                }
            }
        } finally {
            notifyShutdown();
            running.set(false);
        }
    }

    private void notifyTimeout(long availableSequence) {
        try {
            if (timeoutHandler != null) {
                timeoutHandler.onTimeout(availableSequence);
            }
        } catch (Throwable e) {
            exceptionHandler.handleEventException(e, availableSequence, null);
        }
    }

    private void notifyStart() {
        if (eventHandler instanceof Lifecycle) {
            try {
                ((Lifecycle) eventHandler).initialize();
            } catch (Throwable ex) {
                exceptionHandler.handleOnStartException(ex);
            }
        }
    }

    private void notifyShutdown() {
        if (eventHandler instanceof Lifecycle) {
            try {
                ((Lifecycle) eventHandler).destroy();
            } catch (Throwable ex) {
                exceptionHandler.handleOnShutdownException(ex);
            }
        }
    }
}