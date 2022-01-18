package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:58
 */
@FunctionalInterface
public interface ThrowableAsyncBinaryOperator<T> extends ThrowableAsyncBiFunction<T, T, T> {

    default ThrowableAsyncBinaryOperator<T> andThen(ThrowableAsyncUnaryOperator<T> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(r -> ThrowableAsyncUnaryOperator.execute(r, a));
    }

    default ThrowableAsyncBinaryOperator<T> andThenAsync(ThrowableAsyncUnaryOperator<T> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(r -> ThrowableAsyncUnaryOperator.execute(r, a));
    }

    default ThrowableAsyncBinaryOperator<T> andThenAsync(ThrowableAsyncUnaryOperator<T> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(r -> ThrowableAsyncUnaryOperator.execute(r, a), e);
    }

    static <T> CompletableFuture<T> execute(T t1, T t2, ThrowableAsyncBinaryOperator<T> f) throws IllegalArgumentException {
        try {
            return f.apply(t1, t2);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}