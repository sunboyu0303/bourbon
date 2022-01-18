package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:20
 */
@FunctionalInterface
public interface AsyncUnaryOperator<T> extends AsyncFunction<T, T> {

    default AsyncUnaryOperator<T> compose(AsyncUnaryOperator<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    default AsyncUnaryOperator<T> composeAsync(AsyncUnaryOperator<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    default AsyncUnaryOperator<T> composeAsync(AsyncUnaryOperator<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    default AsyncUnaryOperator<T> andThen(AsyncUnaryOperator<T> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(a::apply);
    }

    default AsyncUnaryOperator<T> andThenAsync(AsyncUnaryOperator<T> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(a::apply);
    }

    default AsyncUnaryOperator<T> andThenAsync(AsyncUnaryOperator<T> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(a::apply, e);
    }

    static <T> AsyncUnaryOperator<T> identity() {
        return CompletableFuture::completedFuture;
    }
}