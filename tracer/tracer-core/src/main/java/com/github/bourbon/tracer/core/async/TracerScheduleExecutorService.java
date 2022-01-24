package com.github.bourbon.tracer.core.async;

import com.github.bourbon.tracer.core.context.trace.SofaTraceContext;
import com.github.bourbon.tracer.core.holder.SofaTraceContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:22
 */
public class TracerScheduleExecutorService extends TracedExecutorService implements ScheduledExecutorService {

    public TracerScheduleExecutorService(ScheduledExecutorService delegate) {
        super(delegate, SofaTraceContextHolder.getSofaTraceContext());
    }

    public TracerScheduleExecutorService(ScheduledExecutorService delegate, SofaTraceContext traceContext) {
        super(delegate, traceContext);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return getScheduledExecutorService().schedule(new SofaTracerRunnable(command, traceContext), delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return getScheduledExecutorService().schedule(new SofaTracerCallable<>(callable, traceContext), delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return getScheduledExecutorService().scheduleAtFixedRate(new SofaTracerRunnable(command, traceContext), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return getScheduledExecutorService().scheduleWithFixedDelay(new SofaTracerRunnable(command, traceContext), initialDelay, delay, unit);
    }

    private ScheduledExecutorService getScheduledExecutorService() {
        return (ScheduledExecutorService) delegate;
    }
}