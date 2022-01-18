package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:20
 */
@FunctionalInterface
public interface ThrowableAsyncObjIntConsumer<T> extends ThrowableAsyncBiConsumer<T, Integer> {

    default ThrowableAsyncObjIntConsumer<T> andThen(ThrowableAsyncObjIntConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRun(ThrowableAsyncBiConsumer.of(l, r, a));
    }

    default ThrowableAsyncObjIntConsumer<T> andThenAsync(ThrowableAsyncObjIntConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRunAsync(ThrowableAsyncBiConsumer.of(l, r, a));
    }

    default ThrowableAsyncObjIntConsumer<T> andThenAsync(ThrowableAsyncObjIntConsumer<T> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (l, r) -> accept(l, r).thenRunAsync(ThrowableAsyncBiConsumer.of(l, r, a), e);
    }

    default ThrowableAsyncObjIntConsumer<T> compose(ThrowableAsyncObjIntConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRun(ThrowableAsyncBiConsumer.of(l, r, this));
    }

    default ThrowableAsyncObjIntConsumer<T> composeAsync(ThrowableAsyncObjIntConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRunAsync(ThrowableAsyncBiConsumer.of(l, r, this));
    }

    default ThrowableAsyncObjIntConsumer<T> composeAsync(ThrowableAsyncObjIntConsumer<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return (l, r) -> b.accept(l, r).thenRunAsync(ThrowableAsyncBiConsumer.of(l, r, this), e);
    }

    static <T> CompletableFuture<Void> execute(T t, Integer u, ThrowableAsyncObjIntConsumer<T> c) throws IllegalArgumentException {
        try {
            return c.accept(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}