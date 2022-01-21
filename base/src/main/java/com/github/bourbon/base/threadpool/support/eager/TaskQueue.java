package com.github.bourbon.base.threadpool.support.eager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 10:30
 */
class TaskQueue extends LinkedBlockingQueue<Runnable> {
    private static final long serialVersionUID = 8453813891610983447L;

    private transient EagerThreadPoolExecutor executor;

    TaskQueue(int capacity) {
        super(capacity);
    }

    TaskQueue setExecutor(EagerThreadPoolExecutor executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public boolean offer(Runnable runnable) {
        if (executor == null) {
            throw new RejectedExecutionException("The task queue does not have executor!");
        }
        // worker queue size
        int currentPoolThreadSize = executor.getPoolSize();
        // have free worker. put task into queue to let the worker deal with task.
        if (executor.getSubmittedTaskCount() < currentPoolThreadSize) {
            return super.offer(runnable);
        }
        // return false to let executor create new worker.
        if (currentPoolThreadSize < executor.getMaximumPoolSize()) {
            return false;
        }
        // currentPoolThreadSize >= max
        return super.offer(runnable);
    }

    boolean retryOffer(Runnable runnable, long timeout, TimeUnit unit) throws InterruptedException, RejectedExecutionException {
        if (executor == null) {
            throw new RejectedExecutionException("The task queue does not have executor!");
        }
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown!");
        }
        return super.offer(runnable, timeout, unit);
    }
}