package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:00
 */
@FunctionalInterface
public interface AsyncObjLongConsumer<T> extends AsyncBiConsumer<T, Long> {

    default AsyncObjLongConsumer<T> andThen(AsyncObjLongConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRun(() -> a.accept(l, r));
    }

    default AsyncObjLongConsumer<T> andThenAsync(AsyncObjLongConsumer<T> a) {
        ObjectUtils.requireNonNull(a);
        return (l, r) -> accept(l, r).thenRunAsync(() -> a.accept(l, r));
    }

    default AsyncObjLongConsumer<T> andThenAsync(AsyncObjLongConsumer<T> a, Executor e) {
        ObjectUtils.requireNonNull(a);
        ObjectUtils.requireNonNull(e);
        return (l, r) -> accept(l, r).thenRunAsync(() -> a.accept(l, r), e);
    }

    default AsyncObjLongConsumer<T> compose(AsyncObjLongConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRun(() -> accept(l, r));
    }

    default AsyncObjLongConsumer<T> composeAsync(AsyncObjLongConsumer<T> b) {
        ObjectUtils.requireNonNull(b);
        return (l, r) -> b.accept(l, r).thenRunAsync(() -> accept(l, r));
    }

    default AsyncObjLongConsumer<T> composeAsync(AsyncObjLongConsumer<T> b, Executor e) {
        ObjectUtils.requireNonNull(b);
        ObjectUtils.requireNonNull(e);
        return (l, r) -> b.accept(l, r).thenRunAsync(() -> accept(l, r), e);
    }
}