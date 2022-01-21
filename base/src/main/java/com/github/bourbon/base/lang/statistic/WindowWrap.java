package com.github.bourbon.base.lang.statistic;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.lang.mutable.MutableObject;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/3 11:17
 */
public final class WindowWrap<T> extends MutableObject<T> {

    private static final long serialVersionUID = 8103610188766340083L;
    private final long windowLengthInMs;
    private volatile long windowStart;

    public WindowWrap(long windowLengthInMs, T value) {
        this(windowLengthInMs, Clock.currentTimeMillis(), value);
    }

    public WindowWrap(long windowLengthInMs, long windowStart, T value) {
        super(value);
        this.windowLengthInMs = windowLengthInMs;
        this.windowStart = windowStart;
    }

    public long windowLength() {
        return windowLengthInMs;
    }

    public long windowStart() {
        return windowStart;
    }

    public WindowWrap<T> resetTo(long startTime) {
        this.windowStart = startTime;
        return this;
    }

    public boolean isTimeInWindow(long timeMillis) {
        return windowStart <= timeMillis && timeMillis < windowStart + windowLengthInMs;
    }
}