package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:07
 */
@FunctionalInterface
public interface ThrowableAsyncConsumer<T> {

    CompletableFuture<Void> accept(T t) throws Exception;

    default ThrowableAsyncConsumer<T> andThen(ThrowableAsyncConsumer<? super T> a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRun(of(t, a));
    }

    default ThrowableAsyncConsumer<T> andThenAsync(ThrowableAsyncConsumer<? super T> a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRunAsync(of(t, a));
    }

    default ThrowableAsyncConsumer<T> andThenAsync(ThrowableAsyncConsumer<? super T> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> accept(t).thenRunAsync(of(t, a), e);
    }

    default ThrowableAsyncConsumer<T> compose(ThrowableAsyncConsumer<? super T> b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRun(of(t, this));
    }

    default ThrowableAsyncConsumer<T> composeAsync(ThrowableAsyncConsumer<? super T> b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRunAsync(of(t, this));
    }

    default ThrowableAsyncConsumer<T> composeAsync(ThrowableAsyncConsumer<? super T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return t -> b.accept(t).thenRunAsync(of(t, this), e);
    }

    static <T> ThrowableAsyncConsumerRunnableWrapper<T> of(T t, ThrowableAsyncConsumer<T> c) {
        return new ThrowableAsyncConsumerRunnableWrapper<>(t, c);
    }

    static <T> CompletableFuture<Void> execute(T t, ThrowableAsyncConsumer<T> c) throws IllegalArgumentException {
        try {
            return c.accept(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    class ThrowableAsyncConsumerRunnableWrapper<T> implements Runnable {
        private T t;
        private ThrowableAsyncConsumer<T> c;

        private ThrowableAsyncConsumerRunnableWrapper(T t, ThrowableAsyncConsumer<T> c) {
            this.t = t;
            this.c = c;
        }

        @Override
        public void run() {
            try {
                c.accept(t);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}