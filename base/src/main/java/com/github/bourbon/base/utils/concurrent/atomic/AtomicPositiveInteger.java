package com.github.bourbon.base.utils.concurrent.atomic;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.IntUtils;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 23:41
 */
public class AtomicPositiveInteger extends Number {

    private static final long serialVersionUID = -6230679468795021701L;

    private static final AtomicIntegerFieldUpdater<AtomicPositiveInteger> INDEX_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(AtomicPositiveInteger.class, "index");

    @SuppressWarnings("unused")
    private volatile int index = 0;

    public AtomicPositiveInteger() {
    }

    public AtomicPositiveInteger(int initialValue) {
        INDEX_UPDATER.set(this, initialValue);
    }

    public final int getAndIncrement() {
        return INDEX_UPDATER.getAndIncrement(this) & Integer.MAX_VALUE;
    }

    public final int getAndDecrement() {
        return INDEX_UPDATER.getAndDecrement(this) & Integer.MAX_VALUE;
    }

    public final int incrementAndGet() {
        return INDEX_UPDATER.incrementAndGet(this) & Integer.MAX_VALUE;
    }

    public final int decrementAndGet() {
        return INDEX_UPDATER.decrementAndGet(this) & Integer.MAX_VALUE;
    }

    public final int get() {
        return INDEX_UPDATER.get(this) & Integer.MAX_VALUE;
    }

    public final AtomicPositiveInteger set(int newValue) {
        INDEX_UPDATER.set(this, IntUtils.checkPositiveOrZero(newValue, "newValue"));
        return this;
    }

    public final int getAndSet(int newValue) {
        return INDEX_UPDATER.getAndSet(this, IntUtils.checkPositiveOrZero(newValue, "newValue")) & Integer.MAX_VALUE;
    }

    public final int getAndAdd(int delta) {
        return INDEX_UPDATER.getAndAdd(this, IntUtils.checkPositiveOrZero(delta, "delta")) & Integer.MAX_VALUE;
    }

    public final int addAndGet(int delta) {
        return INDEX_UPDATER.addAndGet(this, IntUtils.checkPositiveOrZero(delta, "delta")) & Integer.MAX_VALUE;
    }

    public final boolean compareAndSet(int expect, int update) {
        return INDEX_UPDATER.compareAndSet(this, expect, IntUtils.checkPositiveOrZero(update, "update"));
    }

    public final boolean weakCompareAndSet(int expect, int update) {
        return INDEX_UPDATER.weakCompareAndSet(this, expect, IntUtils.checkPositiveOrZero(update, "update"));
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
        return get();
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
        return Integer.toString(get());
    }

    @Override
    public int hashCode() {
        return 31 + get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return BooleanUtils.defaultIfAssignableFrom(obj, AtomicPositiveInteger.class, o -> intValue() == ((AtomicPositiveInteger) o).intValue(), false);
    }
}