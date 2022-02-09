package com.github.bourbon.tracer.core.reporter.stat.model;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 10:42
 */
public class StatValues {

    private final AtomicReference<long[]> values = new AtomicReference<>();

    public StatValues(long[] values) {
        this.values.set(values);
    }

    public void update(long[] update) {
        long[] current;
        long[] tmp = new long[update.length];
        do {
            current = values.get();
            for (int k = 0; k < update.length && k < current.length; k++) {
                tmp[k] = current[k] + update[k];
            }
        } while (!values.compareAndSet(current, tmp));
    }

    public void clear(long[] toBeClear) {
        long[] current;
        long[] tmp = new long[toBeClear.length];
        do {
            current = values.get();
            for (int k = 0; k < current.length && k < toBeClear.length; k++) {
                tmp[k] = current[k] - toBeClear[k];
            }
        } while (!values.compareAndSet(current, tmp));
    }

    public long[] getCurrentValue() {
        return values.get();
    }
}