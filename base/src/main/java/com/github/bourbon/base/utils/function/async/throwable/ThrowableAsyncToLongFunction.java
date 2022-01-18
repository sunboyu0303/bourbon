package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:48
 */
@FunctionalInterface
public interface ThrowableAsyncToLongFunction<T> extends ThrowableAsyncFunction<T, Long> {

    default ThrowableAsyncToLongFunction<Long> compose(ThrowableAsyncLongFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(l -> execute(l, this));
    }

    default ThrowableAsyncToLongFunction<Long> composeAsync(ThrowableAsyncLongFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(l -> execute(l, this));
    }

    default ThrowableAsyncToLongFunction<Long> composeAsync(ThrowableAsyncLongFunction<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(l -> execute(l, this), e);
    }

    default ThrowableAsyncToLongFunction<T> andThen(ThrowableAsyncLongFunction<Long> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(l -> ThrowableAsyncLongFunction.execute(l, a));
    }

    default ThrowableAsyncToLongFunction<T> andThenAsync(ThrowableAsyncLongFunction<Long> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(l -> ThrowableAsyncLongFunction.execute(l, a));
    }

    default ThrowableAsyncToLongFunction<T> andThenAsync(ThrowableAsyncLongFunction<Long> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(l -> ThrowableAsyncLongFunction.execute(l, a), e);
    }

    static ThrowableAsyncToLongFunction<Long> identity() {
        return CompletableFuture::completedFuture;
    }

    static <T> CompletableFuture<Long> execute(T t, ThrowableAsyncToLongFunction<T> f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}