package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 18:22
 */
@FunctionalInterface
public interface AsyncDoubleBinaryOperator extends AsyncBinaryOperator<Double> {

    default AsyncDoubleBinaryOperator andThen(AsyncDoubleUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(a::apply);
    }

    default AsyncDoubleBinaryOperator andThenAsync(AsyncDoubleUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply);
    }

    default AsyncDoubleBinaryOperator andThenAsync(AsyncDoubleUnaryOperator a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply, e);
    }
}