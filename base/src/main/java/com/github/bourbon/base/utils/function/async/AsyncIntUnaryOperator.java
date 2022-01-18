package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:42
 */
@FunctionalInterface
public interface AsyncIntUnaryOperator extends AsyncUnaryOperator<Integer> {

    default AsyncIntUnaryOperator compose(AsyncIntUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    default AsyncIntUnaryOperator composeAsync(AsyncIntUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    default AsyncIntUnaryOperator composeAsync(AsyncIntUnaryOperator b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    default AsyncIntUnaryOperator andThen(AsyncIntUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(a::apply);
    }

    default AsyncIntUnaryOperator andThenAsync(AsyncIntUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(a::apply);
    }

    default AsyncIntUnaryOperator andThenAsync(AsyncIntUnaryOperator a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(a::apply, e);
    }

    static AsyncIntUnaryOperator identity() {
        return CompletableFuture::completedFuture;
    }
}