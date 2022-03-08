package com.github.bourbon.base.disruptor.dsl;

import com.github.bourbon.base.disruptor.SequenceBarrier;
import com.github.bourbon.base.disruptor.WorkerPool;
import com.github.bourbon.base.disruptor.lang.Sequence;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 22:48
 */
class WorkerPoolInfo<T> implements ConsumerInfo {
    private final WorkerPool<T> workerPool;
    private final SequenceBarrier sequenceBarrier;
    private boolean endOfChain = true;

    WorkerPoolInfo(WorkerPool<T> workerPool, SequenceBarrier sequenceBarrier) {
        this.workerPool = workerPool;
        this.sequenceBarrier = sequenceBarrier;
    }

    @Override
    public Sequence[] getSequences() {
        return workerPool.getWorkerSequences();
    }

    @Override
    public SequenceBarrier getBarrier() {
        return sequenceBarrier;
    }

    @Override
    public boolean isEndOfChain() {
        return endOfChain;
    }

    @Override
    public void start(Executor executor) {
        workerPool.start(executor);
    }

    @Override
    public void halt() {
        workerPool.halt();
    }

    @Override
    public void markAsUsedInBarrier() {
        endOfChain = false;
    }

    @Override
    public boolean isRunning() {
        return workerPool.isRunning();
    }
}