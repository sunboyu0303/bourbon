package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:24
 */
@FunctionalInterface
public interface ThrowableAsyncDoubleUnaryOperator extends ThrowableAsyncUnaryOperator<Double> {

    default ThrowableAsyncDoubleUnaryOperator compose(ThrowableAsyncDoubleUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(d -> execute(d, this));
    }

    default ThrowableAsyncDoubleUnaryOperator composeAsync(ThrowableAsyncDoubleUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(d -> execute(d, this));
    }

    default ThrowableAsyncDoubleUnaryOperator composeAsync(ThrowableAsyncDoubleUnaryOperator b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(d -> execute(d, this), e);
    }

    default ThrowableAsyncDoubleUnaryOperator andThen(ThrowableAsyncDoubleUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(d -> execute(d, a));
    }

    default ThrowableAsyncDoubleUnaryOperator andThenAsync(ThrowableAsyncDoubleUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(d -> execute(d, a));
    }

    default ThrowableAsyncDoubleUnaryOperator andThenAsync(ThrowableAsyncDoubleUnaryOperator a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(d -> execute(d, a), e);
    }

    static ThrowableAsyncDoubleUnaryOperator identity() {
        return CompletableFuture::completedFuture;
    }

    static CompletableFuture<Double> execute(Double d, ThrowableAsyncDoubleUnaryOperator f) throws IllegalArgumentException {
        try {
            return f.apply(d);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}