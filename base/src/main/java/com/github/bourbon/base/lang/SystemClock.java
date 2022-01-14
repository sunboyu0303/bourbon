package com.github.bourbon.base.lang;

import com.github.bourbon.base.threadpool.dynamic.ExecutorFactory;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 11:29
 */
public final class SystemClock {

    private final AtomicLong now;

    private SystemClock() {
        now = new AtomicLong(System.currentTimeMillis());
        ExecutorFactory.Managed.newSingleScheduledExecutorService("base", new NamedThreadFactory("SystemClock", true))
                .scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), 1L, 1L, TimeUnit.MILLISECONDS);
    }

    private static class InstanceHolder {
        private static final SystemClock INSTANCE = new SystemClock();
    }

    public static long currentTimeMillis() {
        return InstanceHolder.INSTANCE.now.get();
    }
}