package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:34
 */
@FunctionalInterface
public interface AsyncIntBinaryOperator extends AsyncBinaryOperator<Integer> {

    default AsyncIntBinaryOperator andThen(AsyncIntUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(a::apply);
    }

    default AsyncIntBinaryOperator andThenAsync(AsyncIntUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply);
    }

    default AsyncIntBinaryOperator andThenAsync(AsyncIntUnaryOperator a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(a::apply, e);
    }
}