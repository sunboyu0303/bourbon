package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:55
 */
@FunctionalInterface
public interface AsyncObjDoubleConsumer<T> extends AsyncBiConsumer<T, Double> {

    default AsyncObjDoubleConsumer<T> andThen(AsyncObjDoubleConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRun(() -> a.accept(l, r));
    }

    default AsyncObjDoubleConsumer<T> andThenAsync(AsyncObjDoubleConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRunAsync(() -> a.accept(l, r));
    }

    default AsyncObjDoubleConsumer<T> andThenAsync(AsyncObjDoubleConsumer<T> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (l, r) -> accept(l, r).thenRunAsync(() -> a.accept(l, r), e);
    }

    default AsyncObjDoubleConsumer<T> compose(AsyncObjDoubleConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRun(() -> accept(l, r));
    }

    default AsyncObjDoubleConsumer<T> composeAsync(AsyncObjDoubleConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRunAsync(() -> accept(l, r));
    }

    default AsyncObjDoubleConsumer<T> composeAsync(AsyncObjDoubleConsumer<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return (l, r) -> b.accept(l, r).thenRunAsync(() -> accept(l, r), e);
    }
}