package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:01
 */
@FunctionalInterface
public interface ThrowableAsyncIntToDoubleFunction extends ThrowableAsyncIntFunction<Double> {

    @Override
    default ThrowableAsyncIntToDoubleFunction compose(ThrowableAsyncIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(i -> execute(i, this));
    }

    @Override
    default ThrowableAsyncIntToDoubleFunction composeAsync(ThrowableAsyncIntFunction<Integer> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(i -> execute(i, this));
    }

    @Override
    default ThrowableAsyncIntToDoubleFunction composeAsync(ThrowableAsyncIntFunction<Integer> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(i -> execute(i, this), e);
    }

    static ThrowableAsyncIntToDoubleFunction identity() {
        return i -> CompletableFuture.completedFuture(i.doubleValue());
    }

    static CompletableFuture<Double> execute(Integer t, ThrowableAsyncIntToDoubleFunction f) throws IllegalArgumentException {
        try {
            return f.apply(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}