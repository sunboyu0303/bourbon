package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.constant.BooleanConstants;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 15:58
 */
@FunctionalInterface
public interface ThrowableAsyncIntPredicate extends ThrowableAsyncPredicate<Integer> {

    default ThrowableAsyncIntPredicate and(ThrowableAsyncIntPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? execute(t, o) : completedFuture(false));
    }

    default ThrowableAsyncIntPredicate andAsync(ThrowableAsyncIntPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? execute(t, o) : completedFuture(false));
    }

    default ThrowableAsyncIntPredicate andAsync(ThrowableAsyncIntPredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? execute(t, o) : completedFuture(false), e);
    }

    @Override
    default ThrowableAsyncIntPredicate negate() {
        return t -> test(t).thenCompose(r -> completedFuture(!r));
    }

    @Override
    default ThrowableAsyncIntPredicate negateAsync() {
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r));
    }

    @Override
    default ThrowableAsyncIntPredicate negateAsync(Executor e) {
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r), e);
    }

    default ThrowableAsyncIntPredicate or(ThrowableAsyncIntPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : execute(t, o));
    }

    default ThrowableAsyncIntPredicate orAsync(ThrowableAsyncIntPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : execute(t, o));
    }

    default ThrowableAsyncIntPredicate orAsync(ThrowableAsyncIntPredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : execute(t, o), e);
    }

    static CompletableFuture<Boolean> execute(Integer t, ThrowableAsyncIntPredicate p) throws IllegalArgumentException {
        try {
            return p.test(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}