package com.github.bourbon.pfinder.profiler.sdk.metric;

import java.io.Closeable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 00:05
 */
public interface MethodExecutionWatcher extends Closeable {

    void fault();

    MethodExecutionWatcher NOOP = new MethodExecutionWatcher() {
        @Override
        public void close() {
        }

        @Override
        public void fault() {
        }
    };
}