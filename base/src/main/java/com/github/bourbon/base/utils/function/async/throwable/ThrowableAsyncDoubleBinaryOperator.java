package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:13
 */
@FunctionalInterface
public interface ThrowableAsyncDoubleBinaryOperator extends ThrowableAsyncBinaryOperator<Double> {

    default ThrowableAsyncDoubleBinaryOperator andThen(ThrowableAsyncDoubleUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(d -> ThrowableAsyncDoubleUnaryOperator.execute(d, a));
    }

    default ThrowableAsyncDoubleBinaryOperator andThenAsync(ThrowableAsyncDoubleUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(d -> ThrowableAsyncDoubleUnaryOperator.execute(d, a));
    }

    default ThrowableAsyncDoubleBinaryOperator andThenAsync(ThrowableAsyncDoubleUnaryOperator a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(d -> ThrowableAsyncDoubleUnaryOperator.execute(d, a), e);
    }

    static CompletableFuture<Double> execute(Double t1, Double t2, ThrowableAsyncDoubleBinaryOperator f) throws IllegalArgumentException {
        try {
            return f.apply(t1, t2);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}