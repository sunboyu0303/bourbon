package com.github.bourbon.base.ttl.threadpool;

import com.github.bourbon.base.ttl.TtlCallable;
import com.github.bourbon.base.ttl.TtlRunnable;
import com.github.bourbon.base.ttl.spi.TtlEnhanced;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 00:49
 */
class ExecutorServiceTtlWrapper extends ExecutorTtlWrapper implements ExecutorService, TtlEnhanced {

    private final ExecutorService e;

    ExecutorServiceTtlWrapper(ExecutorService e, boolean idempotent) {
        super(e, idempotent);
        this.e = e;
    }

    @Override
    public void shutdown() {
        e.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return e.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return e.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return e.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return e.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return e.submit(TtlCallable.get(task, false, idempotent));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return e.submit(TtlRunnable.get(task, false, idempotent), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return e.submit(TtlRunnable.get(task, false, idempotent));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return e.invokeAll(TtlCallable.gets(tasks, false, idempotent));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return e.invokeAll(TtlCallable.gets(tasks, false, idempotent), timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return e.invokeAny(TtlCallable.gets(tasks, false, idempotent));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return e.invokeAny(TtlCallable.gets(tasks, false, idempotent), timeout, unit);
    }

    @Override
    public ExecutorService unwrap() {
        return e;
    }
}