package com.github.bourbon.pfinder.profiler.sdk.trace;

import com.github.bourbon.pfinder.profiler.sdk.PfinderContext;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 21:19
 */
public class TracingExecutorService implements ExecutorService {

    private final ExecutorService origin;

    public TracingExecutorService(ExecutorService origin) {
        this.origin = origin;
    }

    @Override
    public void shutdown() {
        origin.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return origin.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return origin.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return origin.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return origin.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return origin.submit(PfinderContext.asyncWrapper(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return origin.submit(PfinderContext.asyncWrapper(task), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return origin.submit(PfinderContext.asyncWrapper(task));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return origin.invokeAll(tasks.stream().map(PfinderContext::asyncWrapper).collect(Collectors.toList()));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return origin.invokeAll(tasks.stream().map(PfinderContext::asyncWrapper).collect(Collectors.toList()), timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return origin.invokeAny(tasks.stream().map(PfinderContext::asyncWrapper).collect(Collectors.toList()));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return origin.invokeAny(tasks.stream().map(PfinderContext::asyncWrapper).collect(Collectors.toList()), timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        origin.execute(PfinderContext.asyncWrapper(command));
    }
}