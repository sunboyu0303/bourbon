package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:06
 */
@FunctionalInterface
public interface AsyncToIntBiFunction<T, U> extends AsyncBiFunction<T, U, Integer> {

    default AsyncToIntBiFunction<T, U> andThen(AsyncToIntFunction<Integer> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(a::apply);
    }

    default AsyncToIntBiFunction<T, U> andThenAsync(AsyncToIntFunction<Integer> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply);
    }

    default AsyncToIntBiFunction<T, U> andThenAsync(AsyncToIntFunction<Integer> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply, e);
    }
}