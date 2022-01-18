package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:08
 */
@FunctionalInterface
public interface ThrowableAsyncLongConsumer extends ThrowableAsyncConsumer<Long> {

    default ThrowableAsyncLongConsumer andThen(ThrowableAsyncLongConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRun(ThrowableAsyncConsumer.of(t, a));
    }

    default ThrowableAsyncLongConsumer andThenAsync(ThrowableAsyncLongConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, a));
    }

    default ThrowableAsyncLongConsumer andThenAsync(ThrowableAsyncLongConsumer a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, a), e);
    }

    default ThrowableAsyncLongConsumer compose(ThrowableAsyncLongConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRun(ThrowableAsyncConsumer.of(t, this));
    }

    default ThrowableAsyncLongConsumer composeAsync(ThrowableAsyncLongConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, this));
    }

    default ThrowableAsyncLongConsumer composeAsync(ThrowableAsyncLongConsumer b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return t -> b.accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, this), e);
    }

    static CompletableFuture<Void> execute(Long t, ThrowableAsyncLongConsumer c) throws IllegalArgumentException {
        try {
            return c.accept(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}