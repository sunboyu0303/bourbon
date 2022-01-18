package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:25
 */
@FunctionalInterface
public interface ThrowableAsyncUnaryOperator<T> extends ThrowableAsyncFunction<T, T> {

    default ThrowableAsyncUnaryOperator<T> compose(ThrowableAsyncUnaryOperator<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(t -> execute(t, this));
    }

    default ThrowableAsyncUnaryOperator<T> composeAsync(ThrowableAsyncUnaryOperator<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(t -> execute(t, this));
    }

    default ThrowableAsyncUnaryOperator<T> composeAsync(ThrowableAsyncUnaryOperator<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(t -> execute(t, this), e);
    }

    default ThrowableAsyncUnaryOperator<T> andThen(ThrowableAsyncUnaryOperator<T> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(v -> execute(v, a));
    }

    default ThrowableAsyncUnaryOperator<T> andThenAsync(ThrowableAsyncUnaryOperator<T> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(v -> execute(v, a));
    }

    default ThrowableAsyncUnaryOperator<T> andThenAsync(ThrowableAsyncUnaryOperator<T> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(v -> execute(v, a), e);
    }

    static <T> ThrowableAsyncUnaryOperator<T> identity() {
        return CompletableFuture::completedFuture;
    }

    static <T> CompletableFuture<T> execute(T t, ThrowableAsyncUnaryOperator<T> f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}