package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:37
 */
@FunctionalInterface
public interface AsyncIntFunction<R> extends AsyncFunction<Integer, R> {

    default AsyncIntFunction<R> compose(AsyncIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    default AsyncIntFunction<R> composeAsync(AsyncIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    default AsyncIntFunction<R> composeAsync(AsyncIntFunction<Integer> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    static AsyncIntFunction<Integer> identity() {
        return CompletableFuture::completedFuture;
    }
}