package com.github.bourbon.uuid.core.lang;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/30 15:24
 */
public final class Segment {

    private final AtomicLong value = new AtomicLong(0L);

    private final SegmentBuffer buffer;
    private volatile long min = 0L;
    private volatile long max = 0L;

    Segment(SegmentBuffer buffer) {
        this.buffer = buffer;
    }

    public long getValue() {
        return value.get();
    }

    public Segment setValue(long value) {
        this.value.set(value);
        return this;
    }

    public long getAndIncrement() {
        return value.getAndIncrement();
    }

    public boolean refresh() {
        return (max - getValue()) < 0.9 * (max - min);
    }

    public SegmentBuffer getBuffer() {
        return buffer;
    }

    public long getMin() {
        return min;
    }

    public Segment setMin(long min) {
        this.min = min;
        return this;
    }

    public long getMax() {
        return max;
    }

    public Segment setMax(long max) {
        this.max = max;
        return this;
    }
}