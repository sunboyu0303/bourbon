package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:36
 */
@FunctionalInterface
public interface ThrowableAsyncIntBinaryOperator extends ThrowableAsyncBinaryOperator<Integer> {

    default ThrowableAsyncIntBinaryOperator andThen(ThrowableAsyncIntUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenCompose(i -> ThrowableAsyncIntUnaryOperator.execute(i, a));
    }

    default ThrowableAsyncIntBinaryOperator andThenAsync(ThrowableAsyncIntUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return (t, u) -> apply(t, u).thenComposeAsync(i -> ThrowableAsyncIntUnaryOperator.execute(i, a));
    }

    default ThrowableAsyncIntBinaryOperator andThenAsync(ThrowableAsyncIntUnaryOperator a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (t, u) -> apply(t, u).thenComposeAsync(i -> ThrowableAsyncIntUnaryOperator.execute(i, a), e);
    }

    static CompletableFuture<Integer> execute(Integer t1, Integer t2, ThrowableAsyncIntBinaryOperator f) throws IllegalArgumentException {
        try {
            return f.apply(t1, t2);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}