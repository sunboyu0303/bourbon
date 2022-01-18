package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:21
 */
@FunctionalInterface
public interface ThrowableAsyncFunction<T, R> {

    CompletableFuture<R> apply(T t) throws Exception;

    default <V> ThrowableAsyncFunction<V, R> compose(ThrowableAsyncFunction<V, T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(r -> execute(r, this));
    }

    default <V> ThrowableAsyncFunction<V, R> composeAsync(ThrowableAsyncFunction<V, T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(r -> execute(r, this));
    }

    default <V> ThrowableAsyncFunction<V, R> composeAsync(ThrowableAsyncFunction<V, T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(r -> execute(r, this), e);
    }

    default <V> ThrowableAsyncFunction<T, V> andThen(ThrowableAsyncFunction<R, V> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(r -> execute(r, a));
    }

    default <V> ThrowableAsyncFunction<T, V> andThenAsync(ThrowableAsyncFunction<R, V> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(r -> execute(r, a));
    }

    default <V> ThrowableAsyncFunction<T, V> andThenAsync(ThrowableAsyncFunction<R, V> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(r -> execute(r, a), e);
    }

    static <T> ThrowableAsyncFunction<T, T> identity() {
        return CompletableFuture::completedFuture;
    }

    static <T, R> CompletableFuture<R> execute(T t, ThrowableAsyncFunction<T, R> f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}