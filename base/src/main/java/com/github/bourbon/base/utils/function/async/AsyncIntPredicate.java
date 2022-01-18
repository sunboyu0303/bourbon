package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.constant.BooleanConstants;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:38
 */
@FunctionalInterface
public interface AsyncIntPredicate extends AsyncPredicate<Integer> {

    default AsyncIntPredicate and(AsyncIntPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? o.test(t) : completedFuture(false));
    }

    default AsyncIntPredicate andAsync(AsyncIntPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? o.test(t) : completedFuture(false));
    }

    default AsyncIntPredicate andAsync(AsyncIntPredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? o.test(t) : completedFuture(false), e);
    }

    @Override
    default AsyncIntPredicate negate() {
        return t -> test(t).thenCompose(r -> completedFuture(!r));
    }

    @Override
    default AsyncIntPredicate negateAsync() {
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r));
    }

    @Override
    default AsyncIntPredicate negateAsync(Executor e) {
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r), e);
    }

    default AsyncIntPredicate or(AsyncIntPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : o.test(t));
    }

    default AsyncIntPredicate orAsync(AsyncIntPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : o.test(t));
    }

    default AsyncIntPredicate orAsync(AsyncIntPredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : o.test(t), e);
    }
}