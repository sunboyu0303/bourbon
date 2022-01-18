package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:44
 */
@FunctionalInterface
public interface AsyncLongConsumer extends AsyncConsumer<Long> {

    default AsyncLongConsumer andThen(AsyncLongConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRun(() -> a.accept(t));
    }

    default AsyncLongConsumer andThenAsync(AsyncLongConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRunAsync(() -> a.accept(t));
    }

    default AsyncLongConsumer andThenAsync(AsyncLongConsumer a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> accept(t).thenRunAsync(() -> a.accept(t), e);
    }

    default AsyncLongConsumer compose(AsyncLongConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRun(() -> accept(t));
    }

    default AsyncLongConsumer composeAsync(AsyncLongConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRunAsync(() -> accept(t));
    }

    default AsyncLongConsumer composeAsync(AsyncLongConsumer b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return t -> b.accept(t).thenRunAsync(() -> accept(t), e);
    }
}