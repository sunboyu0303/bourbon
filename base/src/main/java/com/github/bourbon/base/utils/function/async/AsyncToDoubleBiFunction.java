package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:04
 */
@FunctionalInterface
public interface AsyncToDoubleBiFunction<T, U> extends AsyncBiFunction<T, U, Double> {

    default AsyncToDoubleBiFunction<T, U> andThen(AsyncToDoubleFunction<Double> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(a::apply);
    }

    default AsyncToDoubleBiFunction<T, U> andThenAsync(AsyncToDoubleFunction<Double> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply);
    }

    default AsyncToDoubleBiFunction<T, U> andThenAsync(AsyncToDoubleFunction<Double> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply, e);
    }
}