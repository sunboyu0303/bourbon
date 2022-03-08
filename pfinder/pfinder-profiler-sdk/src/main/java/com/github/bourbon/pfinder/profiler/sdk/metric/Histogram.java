package com.github.bourbon.pfinder.profiler.sdk.metric;

import java.util.function.Consumer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 00:02
 */
public interface Histogram extends Metric {

    MethodExecutionWatcher watcher();

    void watch(Consumer<MethodExecutionWatcher> consumer);

    Histogram NOOP = new Histogram() {

        @Override
        public MethodExecutionWatcher watcher() {
            return MethodExecutionWatcher.NOOP;
        }

        @Override
        public void watch(Consumer<MethodExecutionWatcher> consumer) {
            consumer.accept(MethodExecutionWatcher.NOOP);
        }
    };
}