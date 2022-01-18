package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:14
 */
@FunctionalInterface
public interface ThrowableAsyncLongToIntFunction extends ThrowableAsyncLongFunction<Integer> {

    @Override
    default ThrowableAsyncLongToIntFunction compose(ThrowableAsyncLongFunction<Long> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(l -> execute(l, this));
    }

    @Override
    default ThrowableAsyncLongToIntFunction composeAsync(ThrowableAsyncLongFunction<Long> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(l -> execute(l, this));
    }

    @Override
    default ThrowableAsyncLongToIntFunction composeAsync(ThrowableAsyncLongFunction<Long> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(l -> execute(l, this), e);
    }

    static ThrowableAsyncLongToIntFunction identity() {
        return l -> CompletableFuture.completedFuture(l.intValue());
    }

    static CompletableFuture<Integer> execute(Long t, ThrowableAsyncLongToIntFunction f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}