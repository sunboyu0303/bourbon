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
public interface ThrowableAsyncToIntFunction<T> extends ThrowableAsyncFunction<T, Integer> {

    default ThrowableAsyncToIntFunction<Integer> compose(ThrowableAsyncIntFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(i -> execute(i, this));
    }

    default ThrowableAsyncToIntFunction<Integer> composeAsync(ThrowableAsyncIntFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(i -> execute(i, this));
    }

    default ThrowableAsyncToIntFunction<Integer> composeAsync(ThrowableAsyncIntFunction<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(i -> execute(i, this), e);
    }

    default ThrowableAsyncToIntFunction<T> andThen(ThrowableAsyncIntFunction<Integer> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(i -> ThrowableAsyncIntFunction.execute(i, a));
    }

    default ThrowableAsyncToIntFunction<T> andThenAsync(ThrowableAsyncIntFunction<Integer> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(i -> ThrowableAsyncIntFunction.execute(i, a));
    }

    default ThrowableAsyncToIntFunction<T> andThenAsync(ThrowableAsyncIntFunction<Integer> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(i -> ThrowableAsyncIntFunction.execute(i, a), e);
    }

    static ThrowableAsyncToIntFunction<Integer> identity() {
        return CompletableFuture::completedFuture;
    }

    static <T> CompletableFuture<Integer> execute(T t, ThrowableAsyncToIntFunction<T> f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}