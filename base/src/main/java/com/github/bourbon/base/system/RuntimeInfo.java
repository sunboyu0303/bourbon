package com.github.bourbon.base.system;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 17:53
 */
public class RuntimeInfo {

    private final Runtime runtime = Runtime.getRuntime();

    RuntimeInfo() {
    }

    public final Runtime getRuntime() {
        return runtime;
    }

    public final long getMaxMemory() {
        return runtime.maxMemory();
    }

    public final long getTotalMemory() {
        return runtime.totalMemory();
    }

    public final long getFreeMemory() {
        return runtime.freeMemory();
    }

    public final long getUsableMemory() {
        return runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory();
    }

    public final int availableProcessors() {
        return runtime.availableProcessors();
    }
}