package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:44
 */
@FunctionalInterface
public interface ThrowableAsyncToDoubleFunction<T> extends ThrowableAsyncFunction<T, Double> {

    default ThrowableAsyncToDoubleFunction<Double> compose(ThrowableAsyncDoubleFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(t -> execute(t, this));
    }

    default ThrowableAsyncToDoubleFunction<Double> composeAsync(ThrowableAsyncDoubleFunction<T> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(t -> execute(t, this));
    }

    default ThrowableAsyncToDoubleFunction<Double> composeAsync(ThrowableAsyncDoubleFunction<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(t -> execute(t, this), e);
    }

    default ThrowableAsyncToDoubleFunction<T> andThen(ThrowableAsyncDoubleFunction<Double> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(d -> ThrowableAsyncDoubleFunction.execute(d, a));
    }

    default ThrowableAsyncToDoubleFunction<T> andThenAsync(ThrowableAsyncDoubleFunction<Double> a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(d -> ThrowableAsyncDoubleFunction.execute(d, a));
    }

    default ThrowableAsyncToDoubleFunction<T> andThenAsync(ThrowableAsyncDoubleFunction<Double> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(d -> ThrowableAsyncDoubleFunction.execute(d, a), e);
    }

    static ThrowableAsyncToDoubleFunction<Double> identity() {
        return CompletableFuture::completedFuture;
    }

    static <T> CompletableFuture<Double> execute(T t, ThrowableAsyncToDoubleFunction<T> f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}