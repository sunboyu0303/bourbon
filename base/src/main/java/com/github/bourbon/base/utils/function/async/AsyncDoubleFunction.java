package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 18:36
 */
@FunctionalInterface
public interface AsyncDoubleFunction<R> extends AsyncFunction<Double, R> {

    default AsyncDoubleFunction<R> compose(AsyncDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    default AsyncDoubleFunction<R> composeAsync(AsyncDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    default AsyncDoubleFunction<R> composeAsync(AsyncDoubleFunction<Double> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    static AsyncDoubleFunction<Double> identity() {
        return CompletableFuture::completedFuture;
    }
}