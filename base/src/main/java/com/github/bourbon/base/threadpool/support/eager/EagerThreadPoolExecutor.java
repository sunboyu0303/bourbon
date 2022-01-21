package com.github.bourbon.base.threadpool.support.eager;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 10:33
 */
class EagerThreadPoolExecutor extends ThreadPoolExecutor {

    private final AtomicInteger submittedTaskCount = new AtomicInteger(0);

    EagerThreadPoolExecutor(int c, int m, long k, TimeUnit u, TaskQueue q, ThreadFactory f, RejectedExecutionHandler h) {
        super(c, m, k, u, q, f, h);
    }

    int getSubmittedTaskCount() {
        return submittedTaskCount.get();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTaskCount.decrementAndGet();
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException();
        }
        submittedTaskCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException rx) {
            try {
                if (!((TaskQueue) super.getQueue()).retryOffer(command, 0, TimeUnit.MILLISECONDS)) {
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException("Queue capacity is full.", rx);
                }
            } catch (InterruptedException x) {
                submittedTaskCount.decrementAndGet();
                throw new RejectedExecutionException(x);
            }
        } catch (Exception t) {
            submittedTaskCount.decrementAndGet();
            throw t;
        }
    }
}