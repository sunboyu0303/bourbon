package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:02
 */
@FunctionalInterface
public interface ThrowableAsyncIntToLongFunction extends ThrowableAsyncIntFunction<Long> {

    @Override
    default ThrowableAsyncIntToLongFunction compose(ThrowableAsyncIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(i -> execute(i, this));
    }

    @Override
    default ThrowableAsyncIntToLongFunction composeAsync(ThrowableAsyncIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(i -> execute(i, this));
    }

    @Override
    default ThrowableAsyncIntToLongFunction composeAsync(ThrowableAsyncIntFunction<Integer> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(i -> execute(i, this), e);
    }

    static ThrowableAsyncIntToLongFunction identity() {
        return i -> CompletableFuture.completedFuture(i.longValue());
    }

    static CompletableFuture<Long> execute(Integer t, ThrowableAsyncIntToLongFunction f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}