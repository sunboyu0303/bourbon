package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 18:13
 */
@FunctionalInterface
public interface AsyncBinaryOperator<T> extends AsyncBiFunction<T, T, T> {

    default AsyncBinaryOperator<T> andThen(AsyncUnaryOperator<T> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(a::apply);
    }

    default AsyncBinaryOperator<T> andThenAsync(AsyncUnaryOperator<T> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply);
    }

    default AsyncBinaryOperator<T> andThenAsync(AsyncUnaryOperator<T> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply, e);
    }
}