package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:25
 */
@FunctionalInterface
public interface ThrowableAsyncToDoubleBiFunction<T, U> extends ThrowableAsyncBiFunction<T, U, Double> {

    default ThrowableAsyncToDoubleBiFunction<T, U> andThen(ThrowableAsyncToDoubleFunction<Double> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(d -> ThrowableAsyncToDoubleFunction.execute(d, a));
    }

    default ThrowableAsyncToDoubleBiFunction<T, U> andThenAsync(ThrowableAsyncToDoubleFunction<Double> a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(d -> ThrowableAsyncToDoubleFunction.execute(d, a));
    }

    default ThrowableAsyncToDoubleBiFunction<T, U> andThenAsync(ThrowableAsyncToDoubleFunction<Double> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(d -> ThrowableAsyncToDoubleFunction.execute(d, a), e);
    }

    static <T, U> CompletableFuture<Double> execute(T t, U u, ThrowableAsyncToDoubleBiFunction<T, U> f) throws IllegalArgumentException {
        try {
            return f.apply(t, u);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}