package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:24
 */
@FunctionalInterface
public interface AsyncDoubleUnaryOperator extends AsyncUnaryOperator<Double> {

    default AsyncDoubleUnaryOperator compose(AsyncDoubleUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    default AsyncDoubleUnaryOperator composeAsync(AsyncDoubleUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    default AsyncDoubleUnaryOperator composeAsync(AsyncDoubleUnaryOperator b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    default AsyncDoubleUnaryOperator andThen(AsyncDoubleUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(a::apply);
    }

    default AsyncDoubleUnaryOperator andThenAsync(AsyncDoubleUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(a::apply);
    }

    default AsyncDoubleUnaryOperator andThenAsync(AsyncDoubleUnaryOperator a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(a::apply, e);
    }

    static AsyncDoubleUnaryOperator identity() {
        return CompletableFuture::completedFuture;
    }
}