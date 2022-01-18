package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:07
 */
@FunctionalInterface
public interface AsyncToIntFunction<T> extends AsyncFunction<T, Integer> {

    default AsyncToIntFunction<Integer> compose(AsyncIntFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(this::apply);
    }

    default AsyncToIntFunction<Integer> composeAsync(AsyncIntFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(this::apply);
    }

    default AsyncToIntFunction<Integer> composeAsync(AsyncIntFunction<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(this::apply, e);
    }

    default AsyncToIntFunction<T> andThen(AsyncIntFunction<Integer> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(a::apply);
    }

    default AsyncToIntFunction<T> andThenAsync(AsyncIntFunction<Integer> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(a::apply);
    }

    default AsyncToIntFunction<T> andThenAsync(AsyncIntFunction<Integer> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(a::apply, e);
    }


    static AsyncToIntFunction<Integer> identity() {
        return CompletableFuture::completedFuture;
    }
}