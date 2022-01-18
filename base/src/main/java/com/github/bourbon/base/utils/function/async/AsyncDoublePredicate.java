package com.github.bourbon.base.utils.function.async;

import com.github.bourbon.base.constant.BooleanConstants;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 09:14
 */
@FunctionalInterface
public interface AsyncDoublePredicate extends AsyncPredicate<Double> {

    default AsyncDoublePredicate and(AsyncDoublePredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? o.test(t) : completedFuture(false));
    }

    default AsyncDoublePredicate andAsync(AsyncDoublePredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? o.test(t) : completedFuture(false));
    }

    default AsyncDoublePredicate andAsync(AsyncDoublePredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? o.test(t) : completedFuture(false), e);
    }

    @Override
    default AsyncDoublePredicate negate() {
        return t -> test(t).thenCompose(r -> completedFuture(!r));
    }

    @Override
    default AsyncDoublePredicate negateAsync() {
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r));
    }

    @Override
    default AsyncDoublePredicate negateAsync(Executor e) {
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r), e);
    }

    default AsyncDoublePredicate or(AsyncDoublePredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : o.test(t));
    }

    default AsyncDoublePredicate orAsync(AsyncDoublePredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : o.test(t));
    }

    default AsyncDoublePredicate orAsync(AsyncDoublePredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : o.test(t), e);
    }
}