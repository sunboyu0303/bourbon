package com.github.bourbon.uuid.core.lang;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 15:49
 */
public class SegmentBuffer {

    private final SegmentHandler handler;

    private final Segment[] segments = new Segment[]{new Segment(this), new Segment(this)};

    private final AtomicBoolean sw = new AtomicBoolean(false);

    private final AtomicBoolean running = new AtomicBoolean(false);

    private final Lock lock = new ReentrantLock();

    private final AtomicBoolean ready = new AtomicBoolean(false);

    private volatile long step;

    private volatile long updateTimestamp;

    public SegmentBuffer(SegmentHandler handler) {
        this.handler = handler;
        handler.updateSegment(getCurrent());
    }

    public Segment getCurrent() {
        return segments[getCurrentIndex()];
    }

    public SegmentHandler getHandler() {
        return handler;
    }

    public Segment getNext() {
        return segments[getNextIndex()];
    }

    public int getCurrentIndex() {
        return sw.get() ? 1 : 0;
    }

    private int getNextIndex() {
        return sw.get() ? 0 : 1;
    }

    public void switchPos() {
        sw.set(!sw.get());
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public boolean isRunning() {
        return running.get();
    }

    public boolean setRunning() {
        return running.compareAndSet(false, true);
    }

    public SegmentBuffer resetRunning() {
        running.set(false);
        return this;
    }

    public boolean isReady() {
        return ready.get();
    }

    public SegmentBuffer setReady() {
        ready.compareAndSet(false, true);
        return this;
    }

    public SegmentBuffer resetReady() {
        ready.set(false);
        return this;
    }

    public long getStep() {
        return step;
    }

    public SegmentBuffer setStep(long step) {
        this.step = step;
        return this;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public SegmentBuffer setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
        return this;
    }
}