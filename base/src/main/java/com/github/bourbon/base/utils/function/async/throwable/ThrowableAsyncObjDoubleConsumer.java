package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:16
 */
@FunctionalInterface
public interface ThrowableAsyncObjDoubleConsumer<T> extends ThrowableAsyncBiConsumer<T, Double> {

    default ThrowableAsyncObjDoubleConsumer<T> andThen(ThrowableAsyncObjDoubleConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRun(ThrowableAsyncBiConsumer.of(l, r, a));
    }

    default ThrowableAsyncObjDoubleConsumer<T> andThenAsync(ThrowableAsyncObjDoubleConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRunAsync(ThrowableAsyncBiConsumer.of(l, r, a));
    }

    default ThrowableAsyncObjDoubleConsumer<T> andThenAsync(ThrowableAsyncObjDoubleConsumer<T> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (l, r) -> accept(l, r).thenRunAsync(ThrowableAsyncBiConsumer.of(l, r, a), e);
    }

    default ThrowableAsyncObjDoubleConsumer<T> compose(ThrowableAsyncObjDoubleConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRun(ThrowableAsyncBiConsumer.of(l, r, this));
    }

    default ThrowableAsyncObjDoubleConsumer<T> composeAsync(ThrowableAsyncObjDoubleConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRunAsync(ThrowableAsyncBiConsumer.of(l, r, this));
    }

    default ThrowableAsyncObjDoubleConsumer<T> composeAsync(ThrowableAsyncObjDoubleConsumer<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return (l, r) -> b.accept(l, r).thenRunAsync(ThrowableAsyncBiConsumer.of(l, r, this), e);
    }

    static <T> CompletableFuture<Void> execute(T t, Double u, ThrowableAsyncObjDoubleConsumer<T> c) throws IllegalArgumentException {
        try {
            return c.accept(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}