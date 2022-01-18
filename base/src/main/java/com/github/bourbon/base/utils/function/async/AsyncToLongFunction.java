package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:08
 */
@FunctionalInterface
public interface AsyncToLongFunction<T> extends AsyncFunction<T, Long> {

    default AsyncToLongFunction<Long> compose(AsyncLongFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    default AsyncToLongFunction<Long> composeAsync(AsyncLongFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    default AsyncToLongFunction<Long> composeAsync(AsyncLongFunction<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    default AsyncToLongFunction<T> andThen(AsyncLongFunction<Long> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(a::apply);
    }

    default AsyncToLongFunction<T> andThenAsync(AsyncLongFunction<Long> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(a::apply);
    }

    default AsyncToLongFunction<T> andThenAsync(AsyncLongFunction<Long> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(a::apply, e);
    }

    static AsyncToLongFunction<Long> identity() {
        return CompletableFuture::completedFuture;
    }
}