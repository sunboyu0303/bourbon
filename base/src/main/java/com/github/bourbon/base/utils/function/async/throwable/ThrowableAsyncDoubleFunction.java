package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:17
 */
@FunctionalInterface
public interface ThrowableAsyncDoubleFunction<R> extends ThrowableAsyncFunction<Double, R> {

    default ThrowableAsyncDoubleFunction<R> compose(ThrowableAsyncDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(d -> execute(d, this));
    }

    default ThrowableAsyncDoubleFunction<R> composeAsync(ThrowableAsyncDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(d -> execute(d, this));
    }

    default ThrowableAsyncDoubleFunction<R> composeAsync(ThrowableAsyncDoubleFunction<Double> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(d -> execute(d, this), e);
    }

    static ThrowableAsyncDoubleFunction<Double> identity() {
        return CompletableFuture::completedFuture;
    }

    static <R> CompletableFuture<R> execute(Double d, ThrowableAsyncDoubleFunction<R> f) throws IllegalArgumentException {
        try {
            return f.apply(d);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}