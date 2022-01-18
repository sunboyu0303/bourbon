package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:46
 */
@FunctionalInterface
public interface ThrowableAsyncToIntBiFunction<T, U> extends ThrowableAsyncBiFunction<T, U, Integer> {

    default ThrowableAsyncToIntBiFunction<T, U> andThen(ThrowableAsyncToIntFunction<Integer> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(i -> ThrowableAsyncToIntFunction.execute(i, a));
    }

    default ThrowableAsyncToIntBiFunction<T, U> andThenAsync(ThrowableAsyncToIntFunction<Integer> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(i -> ThrowableAsyncToIntFunction.execute(i, a));
    }

    default ThrowableAsyncToIntBiFunction<T, U> andThenAsync(ThrowableAsyncToIntFunction<Integer> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(i -> ThrowableAsyncToIntFunction.execute(i, a), e);
    }

    static <T, U> CompletableFuture<Integer> execute(T t, U u, ThrowableAsyncToIntBiFunction<T, U> f) throws IllegalArgumentException {
        try {
            return f.apply(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}