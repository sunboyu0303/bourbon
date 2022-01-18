package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.constant.BooleanConstants;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:49
 */
@FunctionalInterface
public interface AsyncLongPredicate extends AsyncPredicate<Long> {

    default AsyncLongPredicate and(AsyncLongPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? o.test(t) : completedFuture(false));
    }

    default AsyncLongPredicate andAsync(AsyncLongPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? o.test(t) : completedFuture(false));
    }

    default AsyncLongPredicate andAsync(AsyncLongPredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? o.test(t) : completedFuture(false), e);
    }

    @Override
    default AsyncLongPredicate negate() {
        return t -> test(t).thenCompose(r -> completedFuture(!r));
    }

    @Override
    default AsyncLongPredicate negateAsync() {
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r));
    }

    @Override
    default AsyncLongPredicate negateAsync(Executor e) {
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r), e);
    }

    default AsyncLongPredicate or(AsyncLongPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : o.test(t));
    }

    default AsyncLongPredicate orAsync(AsyncLongPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : o.test(t));
    }

    default AsyncLongPredicate orAsync(AsyncLongPredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : o.test(t), e);
    }
}