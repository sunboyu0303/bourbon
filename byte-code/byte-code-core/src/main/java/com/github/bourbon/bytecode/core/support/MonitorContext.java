package com.github.bourbon.bytecode.core.support;

import com.github.bourbon.base.lang.Clock;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 18:00
 */
public final class MonitorContext {

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void clear() {
        threadLocal.remove();
    }

    public static Long get() {
        return threadLocal.get();
    }

    public static void set() {
        set(Clock.currentTimeMillis());
    }

    public static void set(Long time) {
        threadLocal.set(time);
    }

    private MonitorContext() {
    }
}