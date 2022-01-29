package com.github.bourbon.base.thread;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 14:15
 */
public class ExecutingRunnable implements Runnable {

    private final Runnable originRunnable;

    private final Thread thread;

    private long enqueueTime;

    private long dequeueTime;

    private long finishTime;

    private volatile boolean printed;

    public ExecutingRunnable(Runnable originRunnable, Thread thread) {
        this.originRunnable = ObjectUtils.requireNonNull(originRunnable);
        this.thread = ObjectUtils.requireNonNull(thread);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(this.thread, this.originRunnable);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ExecutingRunnable) {
            ExecutingRunnable er = (ExecutingRunnable) obj;
            return this.thread == er.thread && this.originRunnable == er.originRunnable;
        }
        return false;
    }

    @Override
    public void run() {
        originRunnable.run();
    }

    public long getEnqueueTime() {
        return enqueueTime;
    }

    public ExecutingRunnable setEnqueueTime(long enqueueTime) {
        this.enqueueTime = enqueueTime;
        return this;
    }

    public long getDequeueTime() {
        return dequeueTime;
    }

    public ExecutingRunnable setDequeueTime(long dequeueTime) {
        this.dequeueTime = dequeueTime;
        return this;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public ExecutingRunnable setFinishTime(long finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    public Thread getThread() {
        return thread;
    }

    public boolean isPrinted() {
        return printed;
    }

    public ExecutingRunnable setPrinted(boolean printed) {
        this.printed = printed;
        return this;
    }

    public long getRunningTime() {
        return finishTime - dequeueTime;
    }

    public long getStayInQueueTime() {
        return dequeueTime - enqueueTime;
    }
}