package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:06
 */
@FunctionalInterface
public interface ThrowableAsyncLongBinaryOperator extends ThrowableAsyncBinaryOperator<Long> {

    default ThrowableAsyncLongBinaryOperator andThen(ThrowableAsyncLongUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(l -> ThrowableAsyncLongUnaryOperator.execute(l, a));
    }

    default ThrowableAsyncLongBinaryOperator andThenAsync(ThrowableAsyncLongUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(l -> ThrowableAsyncLongUnaryOperator.execute(l, a));
    }

    default ThrowableAsyncLongBinaryOperator andThenAsync(ThrowableAsyncLongUnaryOperator a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(l -> ThrowableAsyncLongUnaryOperator.execute(l, a), e);
    }

    static CompletableFuture<Long> execute(Long t1, Long t2, ThrowableAsyncLongBinaryOperator f) throws IllegalArgumentException {
        try {
            return f.apply(t1, t2);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}