package com.github.bourbon.pfinder.profiler.sdk.metric;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 23:57
 */
public interface Counter extends Metric {

    void increment();

    void increment(long value);

    Counter NOOP = new Counter() {

        @Override
        public void increment() {
        }

        @Override
        public void increment(long value) {
        }
    };
}