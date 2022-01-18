package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:05
 */
@FunctionalInterface
public interface AsyncToDoubleFunction<T> extends AsyncFunction<T, Double> {

    default AsyncToDoubleFunction<Double> compose(AsyncDoubleFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    default AsyncToDoubleFunction<Double> composeAsync(AsyncDoubleFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    default AsyncToDoubleFunction<Double> composeAsync(AsyncDoubleFunction<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    default AsyncToDoubleFunction<T> andThen(AsyncDoubleFunction<Double> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(a::apply);
    }

    default AsyncToDoubleFunction<T> andThenAsync(AsyncDoubleFunction<Double> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(a::apply);
    }

    default AsyncToDoubleFunction<T> andThenAsync(AsyncDoubleFunction<Double> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(a::apply, e);
    }

    static AsyncToDoubleFunction<Double> identity() {
        return CompletableFuture::completedFuture;
    }
}