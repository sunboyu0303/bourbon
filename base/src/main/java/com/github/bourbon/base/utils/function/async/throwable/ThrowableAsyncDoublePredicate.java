package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.constant.BooleanConstants;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:19
 */
@FunctionalInterface
public interface ThrowableAsyncDoublePredicate extends ThrowableAsyncPredicate<Double> {

    default ThrowableAsyncDoublePredicate and(ThrowableAsyncDoublePredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? execute(t, o) : completedFuture(false));
    }

    default ThrowableAsyncDoublePredicate andAsync(ThrowableAsyncDoublePredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? execute(t, o) : completedFuture(false));
    }

    default ThrowableAsyncDoublePredicate andAsync(ThrowableAsyncDoublePredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? execute(t, o) : completedFuture(false), e);
    }

    @Override
    default ThrowableAsyncDoublePredicate negate() {
        return t -> test(t).thenCompose(r -> completedFuture(!r));
    }

    @Override
    default ThrowableAsyncDoublePredicate negateAsync() {
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r));
    }

    @Override
    default ThrowableAsyncDoublePredicate negateAsync(Executor e) {
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r), e);
    }

    default ThrowableAsyncDoublePredicate or(ThrowableAsyncDoublePredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : execute(t, o));
    }

    default ThrowableAsyncDoublePredicate orAsync(ThrowableAsyncDoublePredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : execute(t, o));
    }

    default ThrowableAsyncDoublePredicate orAsync(ThrowableAsyncDoublePredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : execute(t, o), e);
    }

    static CompletableFuture<Boolean> execute(Double d, ThrowableAsyncDoublePredicate p) throws IllegalArgumentException {
        try {
            return p.test(d);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}