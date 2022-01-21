package com.github.bourbon.base.lang.statistic;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.IntUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.LongUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * Basic data structure for statistic metrics in Sentinel.
 * </p>
 * <p>
 * Leap array use sliding window algorithm to count data. Each bucket cover {@code windowLengthInMs} time span,
 * and the total time span is {@link #intervalInMs}, so the total bucket amount is:
 * {@code sampleCount = intervalInMs / windowLengthInMs}.
 * </p>
 *
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/3 14:48
 */
public abstract class LeapArray<T> {

    protected long windowLengthInMs;
    protected int sampleCount;
    protected long intervalInMs;

    protected final AtomicReferenceArray<WindowWrap<T>> array;

    private final ReentrantLock updateLock = new ReentrantLock();

    protected LeapArray(int sampleCount, long intervalInMs) {
        IntUtils.checkPositive(sampleCount, "sampleCount");
        LongUtils.checkPositive(intervalInMs, "intervalInMs");
        Assert.isTrue(intervalInMs % sampleCount == 0, "time span needs to be evenly divided");

        this.sampleCount = sampleCount;
        this.intervalInMs = intervalInMs;
        this.windowLengthInMs = intervalInMs / sampleCount;
        this.array = new AtomicReferenceArray<>(sampleCount);
    }

    public WindowWrap<T> currentWindow() {
        return currentWindow(Clock.currentTimeMillis());
    }

    public WindowWrap<T> currentWindow(long timeMillis) {
        if (timeMillis < 0) {
            return null;
        }
        int idx = calculateTimeIdx(timeMillis);
        // Calculate current bucket start time.
        long windowStart = calculateWindowStart(timeMillis);
        /*
         * Get bucket item at given time from the array.
         *
         * (1) Bucket is absent, then just create a new bucket and CAS update to circular array.
         * (2) Bucket is up-to-date, then just return the bucket.
         * (3) Bucket is deprecated, then reset current bucket and clean all deprecated buckets.
         */
        while (true) {
            WindowWrap<T> old = array.get(idx);
            if (old == null) {
                /*
                 *     B0       B1      B2    NULL      B4
                 * ||_______|_______|_______|_______|_______||___
                 * 200     400     600     800     1000    1200  timestamp
                 *                             ^
                 *                          time=888
                 *            bucket is empty, so create new and update
                 *
                 * If the old bucket is absent, then we create a new bucket at {@code windowStart},
                 * then try to update circular array via a CAS operation. Only one thread can
                 * succeed to update, while other threads yield its time slice.
                 */
                WindowWrap<T> window = new WindowWrap<>(windowLengthInMs, windowStart, newEmptyBucket(timeMillis));
                if (array.compareAndSet(idx, null, window)) {
                    // Successfully updated, return the created bucket.
                    return window;
                } else {
                    // Contention failed, the thread will yield its time slice to wait for bucket available.
                    Thread.yield();
                }
            } else if (windowStart == old.windowStart()) {
                /*
                 *     B0       B1      B2     B3      B4
                 * ||_______|_______|_______|_______|_______||___
                 * 200     400     600     800     1000    1200  timestamp
                 *                             ^
                 *                          time=888
                 *            startTime of Bucket 3: 800, so it's up-to-date
                 *
                 * If current {@code windowStart} is equal to the start timestamp of old bucket,
                 * that means the time is within the bucket, so directly return the bucket.
                 */
                return old;
            } else if (windowStart > old.windowStart()) {
                /*
                 *   (old)
                 *             B0       B1      B2    NULL      B4
                 * |_______||_______|_______|_______|_______|_______||___
                 * ...    1200     1400    1600    1800    2000    2200  timestamp
                 *                              ^
                 *                           time=1676
                 *          startTime of Bucket 2: 400, deprecated, should be reset
                 *
                 * If the start timestamp of old bucket is behind provided time, that means
                 * the bucket is deprecated. We have to reset the bucket to current {@code windowStart}.
                 * Note that the reset and clean-up operations are hard to be atomic,
                 * so we need a update lock to guarantee the correctness of bucket update.
                 *
                 * The update lock is conditional (tiny scope) and will take effect only when
                 * bucket is deprecated, so in most cases it won't lead to performance loss.
                 */
                if (updateLock.tryLock()) {
                    try {
                        // Successfully get the update lock, now we reset the bucket.
                        return resetWindowTo(old, windowStart);
                    } finally {
                        updateLock.unlock();
                    }
                } else {
                    // Contention failed, the thread will yield its time slice to wait for bucket available.
                    Thread.yield();
                }
            } else {
                // Should not go through here, as the provided time is already behind.
                return new WindowWrap<>(windowLengthInMs, windowStart, newEmptyBucket(timeMillis));
            }
        }
    }

    private int calculateTimeIdx(long timeMillis) {
        // Calculate current index so we can map the timestamp to the leap array.
        return (int) ((timeMillis / windowLengthInMs) % array.length());
    }

    protected long calculateWindowStart(long timeMillis) {
        return timeMillis - timeMillis % windowLengthInMs;
    }

    public WindowWrap<T> getPreviousWindow() {
        return getPreviousWindow(Clock.currentTimeMillis());
    }

    public WindowWrap<T> getPreviousWindow(long timeMillis) {
        if (timeMillis < 0) {
            return null;
        }
        int idx = calculateTimeIdx(timeMillis - windowLengthInMs);
        timeMillis = timeMillis - windowLengthInMs;
        WindowWrap<T> wrap = array.get(idx);
        if (wrap == null || isWindowDeprecated(wrap)) {
            return null;
        }
        if (wrap.windowStart() + windowLengthInMs < (timeMillis)) {
            return null;
        }
        return wrap;
    }

    public boolean isWindowDeprecated(WindowWrap<T> windowWrap) {
        return isWindowDeprecated(Clock.currentTimeMillis(), windowWrap);
    }

    public boolean isWindowDeprecated(long time, WindowWrap<T> windowWrap) {
        return time - windowWrap.windowStart() > intervalInMs;
    }

    public T getWindowValue(long timeMillis) {
        if (timeMillis < 0) {
            return null;
        }
        WindowWrap<T> bucket = array.get(calculateTimeIdx(timeMillis));
        if (bucket == null || !bucket.isTimeInWindow(timeMillis)) {
            return null;
        }
        return bucket.get();
    }

    public List<WindowWrap<T>> list() {
        return list(Clock.currentTimeMillis());
    }

    public List<WindowWrap<T>> list(long validTime) {
        int size = array.length();
        List<WindowWrap<T>> result = ListUtils.newArrayList(size);
        for (int i = 0; i < size; i++) {
            WindowWrap<T> windowWrap = array.get(i);
            if (windowWrap == null || isWindowDeprecated(validTime, windowWrap)) {
                continue;
            }
            result.add(windowWrap);
        }
        return result;
    }

    public List<WindowWrap<T>> listAll() {
        int size = array.length();
        List<WindowWrap<T>> result = ListUtils.newArrayList(size);
        for (int i = 0; i < size; i++) {
            WindowWrap<T> windowWrap = array.get(i);
            if (windowWrap == null) {
                continue;
            }
            result.add(windowWrap);
        }
        return result;
    }

    public List<T> values() {
        return values(Clock.currentTimeMillis());
    }

    public List<T> values(long timeMillis) {
        if (timeMillis < 0) {
            return ListUtils.newArrayList();
        }
        int size = array.length();
        List<T> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            WindowWrap<T> windowWrap = array.get(i);
            if (windowWrap == null || isWindowDeprecated(timeMillis, windowWrap)) {
                continue;
            }
            result.add(windowWrap.get());
        }
        return result;
    }

    public WindowWrap<T> getValidHead() {
        return getValidHead(Clock.currentTimeMillis());
    }

    public WindowWrap<T> getValidHead(long timeMillis) {
        // Calculate index for expected head time.
        WindowWrap<T> wrap = array.get(calculateTimeIdx(timeMillis + windowLengthInMs));
        return wrap == null || isWindowDeprecated(wrap) ? null : wrap;
    }

    public abstract T newEmptyBucket(long timeMillis);

    protected abstract WindowWrap<T> resetWindowTo(WindowWrap<T> windowWrap, long startTime);
}