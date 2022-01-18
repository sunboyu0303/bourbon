package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 15:57
 */
@FunctionalInterface
public interface ThrowableAsyncIntFunction<R> extends ThrowableAsyncFunction<Integer, R> {

    default ThrowableAsyncIntFunction<R> compose(ThrowableAsyncIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(i -> execute(i, this));
    }

    default ThrowableAsyncIntFunction<R> composeAsync(ThrowableAsyncIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(i -> execute(i, this));
    }

    default ThrowableAsyncIntFunction<R> composeAsync(ThrowableAsyncIntFunction<Integer> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(i -> execute(i, this), e);
    }

    static ThrowableAsyncIntFunction<Integer> identity() {
        return CompletableFuture::completedFuture;
    }

    static <R> CompletableFuture<R> execute(Integer t, ThrowableAsyncIntFunction<R> f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}