package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 18:13
 */
@FunctionalInterface
public interface AsyncBiFunction<T, U, R> {

    CompletableFuture<R> apply(T t, U u);

    default <V> AsyncBiFunction<T, U, V> andThen(AsyncFunction<R, V> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(a::apply);
    }

    default <V> AsyncBiFunction<T, U, V> andThenAsync(AsyncFunction<R, V> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply);
    }

    default <V> AsyncBiFunction<T, U, V> andThenAsync(AsyncFunction<R, V> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply, e);
    }
}