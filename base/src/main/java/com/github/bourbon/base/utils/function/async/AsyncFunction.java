package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:16
 */
@FunctionalInterface
public interface AsyncFunction<T, R> {

    CompletableFuture<R> apply(T t);

    default <V> AsyncFunction<V, R> compose(AsyncFunction<V, T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    default <V> AsyncFunction<V, R> composeAsync(AsyncFunction<V, T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    default <V> AsyncFunction<V, R> composeAsync(AsyncFunction<V, T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    default <V> AsyncFunction<T, V> andThen(AsyncFunction<R, V> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(a::apply);
    }

    default <V> AsyncFunction<T, V> andThenAsync(AsyncFunction<R, V> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(a::apply);
    }

    default <V> AsyncFunction<T, V> andThenAsync(AsyncFunction<R, V> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(a::apply, e);
    }

    static <T> AsyncFunction<T, T> identity() {
        return CompletableFuture::completedFuture;
    }
}