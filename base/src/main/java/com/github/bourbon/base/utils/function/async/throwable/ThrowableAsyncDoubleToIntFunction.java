package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:22
 */
@FunctionalInterface
public interface ThrowableAsyncDoubleToIntFunction extends ThrowableAsyncDoubleFunction<Integer> {

    @Override
    default ThrowableAsyncDoubleToIntFunction compose(ThrowableAsyncDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenCompose(d -> execute(d, this));
    }

    @Override
    default ThrowableAsyncDoubleToIntFunction composeAsync(ThrowableAsyncDoubleFunction<Double> b) {
        ObjectUtils.requireNonNull(b);
        return v -> b.apply(v).thenComposeAsync(d -> execute(d, this));
    }

    @Override
    default ThrowableAsyncDoubleToIntFunction composeAsync(ThrowableAsyncDoubleFunction<Double> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return v -> b.apply(v).thenComposeAsync(d -> execute(d, this), e);
    }

    static ThrowableAsyncDoubleToIntFunction identity() {
        return d -> CompletableFuture.completedFuture(d.intValue());
    }

    static CompletableFuture<Integer> execute(Double d, ThrowableAsyncDoubleToIntFunction f) throws IllegalArgumentException {
        try {
            return f.apply(d);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}