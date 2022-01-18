package com.github.bourbon.base.ttl.threadpool;

import com.github.bourbon.base.ttl.TtlCallable;
import com.github.bourbon.base.ttl.TtlRunnable;
import com.github.bourbon.base.ttl.spi.TtlEnhanced;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 11:39
 */
class ScheduledExecutorServiceTtlWrapper extends ExecutorServiceTtlWrapper implements ScheduledExecutorService, TtlEnhanced {
    private final ScheduledExecutorService e;

    ScheduledExecutorServiceTtlWrapper(ScheduledExecutorService e, boolean idempotent) {
        super(e, idempotent);
        this.e = e;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return e.schedule(TtlRunnable.get(command, false, idempotent), delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return e.schedule(TtlCallable.get(callable, false, idempotent), delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return e.scheduleAtFixedRate(TtlRunnable.get(command, false, idempotent), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return e.scheduleWithFixedDelay(TtlRunnable.get(command, false, idempotent), initialDelay, delay, unit);
    }

    @Override
    public ScheduledExecutorService unwrap() {
        return e;
    }
}