package com.github.bourbon.base.cache;

import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.threadpool.dynamic.ExecutorFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 18:52
 */
public enum GlobalPruneTimer {
    INSTANCE;

    private ScheduledExecutorService e = ExecutorFactory.Managed.newSingleScheduledExecutorService(
            "base", new NamedThreadFactory("GlobalPruneTimer", true)
    );

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        return e.scheduleAtFixedRate(r, delay, delay, TimeUnit.MILLISECONDS);
    }
}