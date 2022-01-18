package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:40
 */
@FunctionalInterface
public interface AsyncIntToDoubleFunction extends AsyncIntFunction<Double> {

    @Override
    default AsyncIntToDoubleFunction compose(AsyncIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    @Override
    default AsyncIntToDoubleFunction composeAsync(AsyncIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    @Override
    default AsyncIntToDoubleFunction composeAsync(AsyncIntFunction<Integer> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    static AsyncIntToDoubleFunction identity() {
        return i -> CompletableFuture.completedFuture(i.doubleValue());
    }
}