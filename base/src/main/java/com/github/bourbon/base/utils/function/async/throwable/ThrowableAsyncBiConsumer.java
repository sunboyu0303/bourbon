package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:43
 */
@FunctionalInterface
public interface ThrowableAsyncBiConsumer<T, U> {

    CompletableFuture<Void> accept(T t, U u) throws Exception;

    default ThrowableAsyncBiConsumer<T, U> andThen(ThrowableAsyncBiConsumer<? super T, ? super U> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRun(of(l, r, a));
    }

    default ThrowableAsyncBiConsumer<T, U> andThenAsync(ThrowableAsyncBiConsumer<? super T, ? super U> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRunAsync(of(l, r, a));
    }

    default ThrowableAsyncBiConsumer<T, U> andThenAsync(ThrowableAsyncBiConsumer<? super T, ? super U> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (l, r) -> accept(l, r).thenRunAsync(of(l, r, a), e);
    }

    default ThrowableAsyncBiConsumer<T, U> compose(ThrowableAsyncBiConsumer<? super T, ? super U> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRun(of(l, r, this));
    }

    default ThrowableAsyncBiConsumer<T, U> composeAsync(ThrowableAsyncBiConsumer<? super T, ? super U> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRunAsync(of(l, r, this));
    }

    default ThrowableAsyncBiConsumer<T, U> composeAsync(ThrowableAsyncBiConsumer<? super T, ? super U> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRunAsync(of(l, r, this), e);
    }

    static <T, U> CompletableFuture<Void> execute(T t, U u, ThrowableAsyncBiConsumer<T, U> c) throws IllegalStateException {
        try {
            return c.accept(t, u);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    static <T, U> ThrowableAsyncBiConsumerRunnableWrapper<T, U> of(T t, U u, ThrowableAsyncBiConsumer<T, U> c) {
        return new ThrowableAsyncBiConsumerRunnableWrapper<>(t, u, c);
    }

    class ThrowableAsyncBiConsumerRunnableWrapper<T, U> implements Runnable {
        private T t;
        private U u;
        private ThrowableAsyncBiConsumer<T, U> c;

        private ThrowableAsyncBiConsumerRunnableWrapper(T t, U u, ThrowableAsyncBiConsumer<T, U> c) {
            this.t = t;
            this.u = u;
            this.c = c;
        }

        @Override
        public void run() {
            try {
                c.accept(t, u);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}