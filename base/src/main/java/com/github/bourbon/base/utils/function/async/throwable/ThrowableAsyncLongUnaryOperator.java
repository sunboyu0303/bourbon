package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:15
 */
@FunctionalInterface
public interface ThrowableAsyncLongUnaryOperator extends ThrowableAsyncUnaryOperator<Long> {

    default ThrowableAsyncLongUnaryOperator compose(ThrowableAsyncLongUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(l -> execute(l, this));
    }

    default ThrowableAsyncLongUnaryOperator composeAsync(ThrowableAsyncLongUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(l -> execute(l, this));
    }

    default ThrowableAsyncLongUnaryOperator composeAsync(ThrowableAsyncLongUnaryOperator b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(l -> execute(l, this), e);
    }

    default ThrowableAsyncLongUnaryOperator andThen(ThrowableAsyncLongUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(l -> execute(l, a));
    }

    default ThrowableAsyncLongUnaryOperator andThenAsync(ThrowableAsyncLongUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(l -> execute(l, a));
    }

    default ThrowableAsyncLongUnaryOperator andThenAsync(ThrowableAsyncLongUnaryOperator a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(l -> execute(l, a), e);
    }

    static ThrowableAsyncLongUnaryOperator identity() {
        return CompletableFuture::completedFuture;
    }

    static CompletableFuture<Long> execute(Long t, ThrowableAsyncLongUnaryOperator f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}