package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:14
 */
@FunctionalInterface
public interface ThrowableAsyncDoubleConsumer extends ThrowableAsyncConsumer<Double> {

    default ThrowableAsyncDoubleConsumer andThen(ThrowableAsyncDoubleConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRun(ThrowableAsyncConsumer.of(t, a));
    }

    default ThrowableAsyncDoubleConsumer andThenAsync(ThrowableAsyncDoubleConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, a));
    }

    default ThrowableAsyncDoubleConsumer andThenAsync(ThrowableAsyncDoubleConsumer a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, a), e);
    }

    default ThrowableAsyncDoubleConsumer compose(ThrowableAsyncDoubleConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRun(ThrowableAsyncConsumer.of(t, this));
    }

    default ThrowableAsyncDoubleConsumer composeAsync(ThrowableAsyncDoubleConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, this));
    }

    default ThrowableAsyncDoubleConsumer composeAsync(ThrowableAsyncDoubleConsumer b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return t -> b.accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, this), e);
    }

    static CompletableFuture<Void> execute(Double d, ThrowableAsyncDoubleConsumer c) throws IllegalArgumentException {
        try {
            return c.accept(d);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}