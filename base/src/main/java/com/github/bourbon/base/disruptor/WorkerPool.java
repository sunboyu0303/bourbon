package com.github.bourbon.base.disruptor;

import com.github.bourbon.base.disruptor.handler.ExceptionHandler;
import com.github.bourbon.base.disruptor.handler.WorkHandler;
import com.github.bourbon.base.disruptor.lang.RingBuffer;
import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.disruptor.processor.WorkProcessor;
import com.github.bourbon.base.disruptor.strategy.BlockingWaitStrategy;
import com.github.bourbon.base.utils.Utils;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 10:08
 */
public final class WorkerPool<T> {
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final Sequence workSequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);
    private final RingBuffer<T> ringBuffer;
    private final WorkProcessor<?>[] workProcessors;

    public WorkerPool(RingBuffer<T> ringBuffer, SequenceBarrier sequenceBarrier, ExceptionHandler<? super T> exceptionHandler, WorkHandler<? super T>... workHandlers) {
        this.ringBuffer = ringBuffer;
        final int numWorkers = workHandlers.length;
        workProcessors = new WorkProcessor[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            workProcessors[i] = new WorkProcessor<>(ringBuffer, sequenceBarrier, workHandlers[i], exceptionHandler, workSequence);
        }
    }

    public WorkerPool(EventFactory<T> eventFactory, ExceptionHandler<? super T> exceptionHandler, WorkHandler<? super T>... workHandlers) {
        ringBuffer = RingBuffer.createMultiProducer(eventFactory, 1024, new BlockingWaitStrategy());
        SequenceBarrier barrier = ringBuffer.newBarrier();
        int numWorkers = workHandlers.length;
        workProcessors = new WorkProcessor[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            workProcessors[i] = new WorkProcessor<>(ringBuffer, barrier, workHandlers[i], exceptionHandler, workSequence);
        }
        ringBuffer.addGatingSequences(getWorkerSequences());
    }

    public Sequence[] getWorkerSequences() {
        Sequence[] sequences = new Sequence[workProcessors.length + 1];
        for (int i = 0, size = workProcessors.length; i < size; i++) {
            sequences[i] = workProcessors[i].getSequence();
        }
        sequences[sequences.length - 1] = workSequence;
        return sequences;
    }

    public RingBuffer<T> start(Executor executor) {
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException("WorkerPool has already been started and cannot be restarted until halted.");
        }
        long cursor = ringBuffer.getCursor();
        workSequence.set(cursor);
        for (WorkProcessor<?> processor : workProcessors) {
            processor.getSequence().set(cursor);
            executor.execute(processor);
        }
        return ringBuffer;
    }

    public void drainAndHalt() {
        Sequence[] workerSequences = getWorkerSequences();
        while (ringBuffer.getCursor() > Utils.getMinimumSequence(workerSequences)) {
            Thread.yield();
        }
        for (WorkProcessor<?> processor : workProcessors) {
            processor.halt();
        }
        started.set(false);
    }

    public void halt() {
        for (WorkProcessor<?> processor : workProcessors) {
            processor.halt();
        }
        started.set(false);
    }

    public boolean isRunning() {
        return started.get();
    }
}