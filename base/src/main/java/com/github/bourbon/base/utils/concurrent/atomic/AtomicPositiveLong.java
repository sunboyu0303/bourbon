package com.github.bourbon.base.utils.concurrent.atomic;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.LongUtils;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 23:44
 */
public class AtomicPositiveLong extends Number {

    private static final long serialVersionUID = -396447917170194894L;

    private static final AtomicLongFieldUpdater<AtomicPositiveLong> INDEX_UPDATER =
            AtomicLongFieldUpdater.newUpdater(AtomicPositiveLong.class, "index");

    @SuppressWarnings("unused")
    private volatile long index = 0;

    public AtomicPositiveLong() {
    }

    public AtomicPositiveLong(long initialValue) {
        INDEX_UPDATER.set(this, initialValue);
    }

    public final long getAndIncrement() {
        return INDEX_UPDATER.getAndIncrement(this) & Long.MAX_VALUE;
    }

    public final long getAndDecrement() {
        return INDEX_UPDATER.getAndDecrement(this) & Long.MAX_VALUE;
    }

    public final long incrementAndGet() {
        return INDEX_UPDATER.incrementAndGet(this) & Long.MAX_VALUE;
    }

    public final long decrementAndGet() {
        return INDEX_UPDATER.decrementAndGet(this) & Long.MAX_VALUE;
    }

    public final long get() {
        return INDEX_UPDATER.get(this) & Long.MAX_VALUE;
    }

    public final AtomicPositiveLong set(long newValue) {
        INDEX_UPDATER.set(this, LongUtils.checkPositiveOrZero(newValue, "newValue"));
        return this;
    }

    public final long getAndSet(long newValue) {
        return INDEX_UPDATER.getAndSet(this, LongUtils.checkPositiveOrZero(newValue, "newValue")) & Long.MAX_VALUE;
    }

    public final long getAndAdd(long delta) {
        return INDEX_UPDATER.getAndAdd(this, LongUtils.checkPositiveOrZero(delta, "delta")) & Long.MAX_VALUE;
    }

    public final long addAndGet(long delta) {
        return INDEX_UPDATER.addAndGet(this, LongUtils.checkPositiveOrZero(delta, "delta")) & Long.MAX_VALUE;
    }

    public final boolean compareAndSet(long expect, long update) {
        return INDEX_UPDATER.compareAndSet(this, expect, LongUtils.checkPositiveOrZero(update, "update"));
    }

    public final boolean weakCompareAndSet(long expect, long update) {
        return INDEX_UPDATER.weakCompareAndSet(this, expect, LongUtils.checkPositiveOrZero(update, "update"));
    }

    @Override
    public byte byteValue() {
        return (byte) get();
    }

    @Override
    public short shortValue() {
        return (short) get();
    }

    @Override
    public int intValue() {
        return (int) get();
    }

    @Override
    public long longValue() {
        return get();
    }

    @Override
    public float floatValue() {
        return (float) get();
    }

    @Override
    public double doubleValue() {
        return get();
    }

    @Override
    public String toString() {
        return Long.toString(get());
    }

    @Override
    public int hashCode() {
        return (int) (31 + get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return BooleanUtils.defaultIfAssignableFrom(obj, AtomicPositiveLong.class, o -> intValue() == ((AtomicPositiveLong) o).intValue(), false);
    }
}