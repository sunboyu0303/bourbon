package com.github.bourbon.base.utils.concurrent.atomic;

import com.github.bourbon.base.lang.LongCounter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 11:10
 */
public class AtomicLongCounter extends AtomicLong implements LongCounter {

    private static final long serialVersionUID = 5731994495135844990L;

    public void add(long delta) {
        addAndGet(delta);
    }

    public void increment() {
        incrementAndGet();
    }

    public void decrement() {
        decrementAndGet();
    }

    public long value() {
        return get();
    }
}