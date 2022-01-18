package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 18:24
 */
@FunctionalInterface
public interface AsyncDoubleConsumer extends AsyncConsumer<Double> {

    default AsyncDoubleConsumer andThen(AsyncDoubleConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRun(() -> a.accept(t));
    }

    default AsyncDoubleConsumer andThenAsync(AsyncDoubleConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRunAsync(() -> a.accept(t));
    }

    default AsyncDoubleConsumer andThenAsync(AsyncDoubleConsumer a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> accept(t).thenRunAsync(() -> a.accept(t), e);
    }

    default AsyncDoubleConsumer compose(AsyncDoubleConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRun(() -> accept(t));
    }

    default AsyncDoubleConsumer composeAsync(AsyncDoubleConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRunAsync(() -> accept(t));
    }

    default AsyncDoubleConsumer composeAsync(AsyncDoubleConsumer b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return t -> b.accept(t).thenRunAsync(() -> accept(t), e);
    }
}