package com.github.bourbon.tracer.core.async;

import com.github.bourbon.tracer.core.context.trace.SofaTraceContext;
import com.github.bourbon.tracer.core.holder.SofaTraceContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/24 11:18
 */
public class TracedExecutorService implements ExecutorService {

    protected final ExecutorService delegate;
    protected final SofaTraceContext traceContext;

    public TracedExecutorService(ExecutorService delegate) {
        this(delegate, SofaTraceContextHolder.getSofaTraceContext());
    }

    public TracedExecutorService(ExecutorService delegate, SofaTraceContext traceContext) {
        this.delegate = delegate;
        this.traceContext = traceContext;
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return delegate.submit(new SofaTracerCallable<>(task, traceContext));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return delegate.submit(new SofaTracerRunnable(task, traceContext), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return delegate.submit(new SofaTracerRunnable(task, traceContext));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return delegate.invokeAll(wrapTracerCallableCollection(tasks));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.invokeAll(wrapTracerCallableCollection(tasks), timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return delegate.invokeAny(wrapTracerCallableCollection(tasks));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.invokeAny(wrapTracerCallableCollection(tasks), timeout, unit);
    }

    @Override
    public void execute(final Runnable command) {
        delegate.execute(new SofaTracerRunnable(command, traceContext));
    }

    private <T> Collection<? extends Callable<T>> wrapTracerCallableCollection(Collection<? extends Callable<T>> originalCollection) {
        return originalCollection.stream().map(c -> new SofaTracerCallable<>(c, traceContext)).collect(Collectors.toList());
    }
}