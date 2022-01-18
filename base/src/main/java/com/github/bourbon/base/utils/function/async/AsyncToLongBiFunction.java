package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:07
 */
@FunctionalInterface
public interface AsyncToLongBiFunction<T, U> extends AsyncBiFunction<T, U, Long> {

    default AsyncToLongBiFunction<T, U> andThen(AsyncToLongFunction<Long> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(a::apply);
    }

    default AsyncToLongBiFunction<T, U> andThenAsync(AsyncToLongFunction<Long> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply);
    }

    default AsyncToLongBiFunction<T, U> andThenAsync(AsyncToLongFunction<Long> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply, e);
    }
}