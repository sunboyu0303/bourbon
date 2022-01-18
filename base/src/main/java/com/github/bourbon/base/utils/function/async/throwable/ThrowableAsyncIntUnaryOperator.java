package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:04
 */
@FunctionalInterface
public interface ThrowableAsyncIntUnaryOperator extends ThrowableAsyncUnaryOperator<Integer> {

    default ThrowableAsyncIntUnaryOperator compose(ThrowableAsyncIntUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(i -> execute(i, this));
    }

    default ThrowableAsyncIntUnaryOperator composeAsync(ThrowableAsyncIntUnaryOperator b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(i -> execute(i, this));
    }

    default ThrowableAsyncIntUnaryOperator composeAsync(ThrowableAsyncIntUnaryOperator b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(i -> execute(i, this), e);
    }

    default ThrowableAsyncIntUnaryOperator andThen(ThrowableAsyncIntUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenCompose(i -> execute(i, a));
    }

    default ThrowableAsyncIntUnaryOperator andThenAsync(ThrowableAsyncIntUnaryOperator a) {
        ObjectUtils.requireNonNull(a);
        return t -> apply(t).thenComposeAsync(i -> execute(i, a));
    }

    default ThrowableAsyncIntUnaryOperator andThenAsync(ThrowableAsyncIntUnaryOperator a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> apply(t).thenComposeAsync(i -> execute(i, a), e);
    }

    static ThrowableAsyncIntUnaryOperator identity() {
        return CompletableFuture::completedFuture;
    }

    static CompletableFuture<Integer> execute(Integer t, ThrowableAsyncIntUnaryOperator f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}