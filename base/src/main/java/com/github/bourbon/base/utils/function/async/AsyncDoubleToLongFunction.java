package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:23
 */
@FunctionalInterface
public interface AsyncDoubleToLongFunction extends AsyncDoubleFunction<Long> {

    @Override
    default AsyncDoubleToLongFunction compose(AsyncDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    @Override
    default AsyncDoubleToLongFunction composeAsync(AsyncDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    @Override
    default AsyncDoubleToLongFunction composeAsync(AsyncDoubleFunction<Double> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    static AsyncDoubleToLongFunction identity() {
        return d -> CompletableFuture.completedFuture(d.longValue());
    }
}