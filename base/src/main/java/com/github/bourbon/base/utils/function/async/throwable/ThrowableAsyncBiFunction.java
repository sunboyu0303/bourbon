package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:55
 */
@FunctionalInterface
public interface ThrowableAsyncBiFunction<T, U, R> {

    CompletableFuture<R> apply(T t, U u) throws Exception;

    default <V> ThrowableAsyncBiFunction<T, U, V> andThen(ThrowableAsyncFunction<R, V> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(r -> ThrowableAsyncFunction.execute(r, a));
    }

    default <V> ThrowableAsyncBiFunction<T, U, V> andThenAsync(ThrowableAsyncFunction<R, V> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(r -> ThrowableAsyncFunction.execute(r, a));
    }

    default <V> ThrowableAsyncBiFunction<T, U, V> andThenAsync(ThrowableAsyncFunction<R, V> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(r -> ThrowableAsyncFunction.execute(r, a), e);
    }

    static <T, U, R> CompletableFuture<R> execute(T t, U u, ThrowableAsyncBiFunction<T, U, R> function) throws IllegalArgumentException {
        try {
            return function.apply(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}