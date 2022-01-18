package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:53
 */
@FunctionalInterface
public interface AsyncLongToIntFunction extends AsyncLongFunction<Integer> {

    @Override
    default AsyncLongToIntFunction compose(AsyncLongFunction<Long> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    @Override
    default AsyncLongToIntFunction composeAsync(AsyncLongFunction<Long> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    @Override
    default AsyncLongToIntFunction composeAsync(AsyncLongFunction<Long> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    static AsyncLongToIntFunction identity() {
        return l -> CompletableFuture.completedFuture(l.intValue());
    }
}