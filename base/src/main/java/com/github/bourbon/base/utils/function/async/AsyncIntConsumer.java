package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:35
 */
@FunctionalInterface
public interface AsyncIntConsumer extends AsyncConsumer<Integer> {

    default AsyncIntConsumer andThen(AsyncIntConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRun(() -> a.accept(t));
    }

    default AsyncIntConsumer andThenAsync(AsyncIntConsumer a) {
        ObjectUtils.requireNonNull(a);
        return t -> accept(t).thenRunAsync(() -> a.accept(t));
    }

    default AsyncIntConsumer andThenAsync(AsyncIntConsumer a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return t -> accept(t).thenRunAsync(() -> a.accept(t), e);
    }

    default AsyncIntConsumer compose(AsyncIntConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRun(() -> accept(t));
    }

    default AsyncIntConsumer composeAsync(AsyncIntConsumer b) {
        ObjectUtils.requireNonNull(b);
        return t -> b.accept(t).thenRunAsync(() -> accept(t));
    }

    default AsyncIntConsumer composeAsync(AsyncIntConsumer b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return t -> b.accept(t).thenRunAsync(() -> accept(t), e);
    }
}