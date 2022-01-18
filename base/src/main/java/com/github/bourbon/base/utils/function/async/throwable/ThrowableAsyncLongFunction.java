package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:10
 */
@FunctionalInterface
public interface ThrowableAsyncLongFunction<R> extends ThrowableAsyncFunction<Long, R> {

    default ThrowableAsyncLongFunction<R> compose(ThrowableAsyncLongFunction<Long> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(l -> execute(l, this));
    }

    default ThrowableAsyncLongFunction<R> composeAsync(ThrowableAsyncLongFunction<Long> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(l -> execute(l, this));
    }

    default ThrowableAsyncLongFunction<R> composeAsync(ThrowableAsyncLongFunction<Long> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(l -> execute(l, this), e);
    }

    static ThrowableAsyncLongFunction<Long> identity() {
        return CompletableFuture::completedFuture;
    }

    static <R> CompletableFuture<R> execute(Long t, ThrowableAsyncLongFunction<R> f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}