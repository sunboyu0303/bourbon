package com.github.bourbon.pfinder.profiler.sdk;

import com.github.bourbon.pfinder.profiler.sdk.metric.PfinderMetricRegistry;
import com.github.bourbon.pfinder.profiler.sdk.trace.TracingExecutor;
import com.github.bourbon.pfinder.profiler.sdk.trace.TracingExecutorService;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 17:08
 */
public class PfinderContext {

    static final class InstanceHolder {
        static PfinderContext INSTANCE = new PfinderContext();
    }

    public static PfinderContext getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean isAvailable() {
        return getInstance().available();
    }

    public static boolean isTracing() {
        return getInstance().tracing();
    }

    public static PfinderMetricRegistry getMetricRegistry() {
        return getInstance().metricRegistry();
    }

    public static void onAvailable(PfinderProcessor processor) {
        PfinderContext instance = getInstance();
        if (instance.available()) {
            processor.process(instance);
        }
    }

    public static void onTracing(PfinderProcessor processor) {
        PfinderContext instance = getInstance();
        if (instance.tracing()) {
            processor.process(instance);
        }
    }

    /**
     * @param tracingName If null or empty, then will be use the full class-name be the name
     */
    public static Runnable asyncWrapper(String tracingName, Runnable runnable) {
        PfinderContext instance = getInstance();
        return instance.tracing() ? instance.wrapRunnable(tracingName, runnable) : runnable;
    }

    /**
     * @param tracingName If null or empty, then will be use the full class-name be the name
     */
    public static <T> Callable<T> asyncWrapper(String tracingName, Callable<T> callable) {
        PfinderContext instance = getInstance();
        return instance.tracing() ? instance.wrapRunnable(tracingName, callable) : callable;
    }

    public static Runnable asyncWrapper(Runnable runnable) {
        return asyncWrapper(null, runnable);
    }

    public static <T> Callable<T> asyncWrapper(Callable<T> callable) {
        return asyncWrapper(null, callable);
    }

    public static ExecutorService executorServiceWrapper(ExecutorService originExecutorService) {
        return new TracingExecutorService(originExecutorService);
    }

    public static Executor executorWrapper(Executor originExecutor) {
        return new TracingExecutor(originExecutor);
    }

    public static Runnable unwrapRunnable(Runnable runnable) {
        return PfinderContextPlugins.UnwrapPlugin.InstanceHolder.INSTANCE.unwrapRunnable(runnable);
    }

    public static <T> Callable<T> unwrapCallable(Callable<T> callable) {
        return PfinderContextPlugins.UnwrapPlugin.InstanceHolder.INSTANCE.unwrapCallable(callable);
    }

    public static boolean forceSample() {
        return PfinderContextPlugins.ForceSamplePlugin.InstanceHolder.INSTANCE.forceSample();
    }

    public String traceId() {
        return "none";
    }

    public boolean available() {
        return false;
    }

    public boolean tracing() {
        return false;
    }

    public TraceTagAppender traceTagAppender() {
        return TraceTagAppender.NOOP;
    }

    public PfinderMetricRegistry metricRegistry() {
        return PfinderMetricRegistry.NOOP;
    }

    protected Runnable wrapRunnable(String tracingName, Runnable runnable) {
        return runnable;
    }

    protected <T> Callable<T> wrapRunnable(String tracingName, Callable<T> callable) {
        return callable;
    }
}