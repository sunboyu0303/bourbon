package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 15:53
 */
@FunctionalInterface
public interface ThrowableAsyncIntConsumer extends ThrowableAsyncConsumer<Integer> {

    default ThrowableAsyncIntConsumer andThen(ThrowableAsyncIntConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRun(ThrowableAsyncConsumer.of(t, a));
    }

    default ThrowableAsyncIntConsumer andThenAsync(ThrowableAsyncIntConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, a));
    }

    default ThrowableAsyncIntConsumer andThenAsync(ThrowableAsyncIntConsumer a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, a), e);
    }

    default ThrowableAsyncIntConsumer compose(ThrowableAsyncIntConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRun(ThrowableAsyncConsumer.of(t, this));
    }

    default ThrowableAsyncIntConsumer composeAsync(ThrowableAsyncIntConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, this));
    }

    default ThrowableAsyncIntConsumer composeAsync(ThrowableAsyncIntConsumer b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return t -> b.accept(t).thenRunAsync(ThrowableAsyncConsumer.of(t, this), e);
    }

    static CompletableFuture<Void> execute(Integer t, ThrowableAsyncIntConsumer c) throws IllegalArgumentException {
        try {
            return c.accept(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}