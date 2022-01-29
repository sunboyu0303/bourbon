package com.github.bourbon.base.thread;

import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 14:15
 */
class ExecutingRunnable implements Runnable {

    private Runnable originRunnable;

    private Thread thread;

    private long enqueueTime;

    private long dequeueTime;

    private long finishTime;

    private volatile boolean printed;

    ExecutingRunnable(Runnable originRunnable) {
        this.originRunnable = ObjectUtils.requireNonNull(originRunnable);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
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
    public String toString() {
        if (thread == null) {
            return originRunnable.toString();
        }
        return originRunnable.toString() + thread.toString();
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

    public ExecutingRunnable setThread(Thread thread) {
        this.thread = thread;
        return this;
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