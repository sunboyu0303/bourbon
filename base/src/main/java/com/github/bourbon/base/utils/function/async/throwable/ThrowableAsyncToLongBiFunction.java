package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:47
 */
@FunctionalInterface
public interface ThrowableAsyncToLongBiFunction<T, U> extends ThrowableAsyncBiFunction<T, U, Long> {

    default ThrowableAsyncToLongBiFunction<T, U> andThen(ThrowableAsyncToLongFunction<Long> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(l -> ThrowableAsyncToLongFunction.execute(l, a));
    }

    default ThrowableAsyncToLongBiFunction<T, U> andThenAsync(ThrowableAsyncToLongFunction<Long> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(l -> ThrowableAsyncToLongFunction.execute(l, a));
    }

    default ThrowableAsyncToLongBiFunction<T, U> andThenAsync(ThrowableAsyncToLongFunction<Long> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(l -> ThrowableAsyncToLongFunction.execute(l, a), e);
    }

    static <T, U> CompletableFuture<Long> execute(T t, U u, ThrowableAsyncToLongBiFunction<T, U> f) throws IllegalArgumentException {
        try {
            return f.apply(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}