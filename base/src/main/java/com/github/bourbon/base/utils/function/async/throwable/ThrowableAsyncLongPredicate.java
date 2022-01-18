package com.github.bourbon.base.utils.function.async.throwable;

import com.github.bourbon.base.constant.BooleanConstants;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:11
 */
@FunctionalInterface
public interface ThrowableAsyncLongPredicate extends ThrowableAsyncPredicate<Long> {

    default ThrowableAsyncLongPredicate and(ThrowableAsyncLongPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? execute(t, o) : completedFuture(false));
    }

    default ThrowableAsyncLongPredicate andAsync(ThrowableAsyncLongPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? execute(t, o) : completedFuture(false));
    }

    default ThrowableAsyncLongPredicate andAsync(ThrowableAsyncLongPredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? execute(t, o) : completedFuture(false), e);
    }

    @Override
    default ThrowableAsyncLongPredicate negate() {
        return t -> test(t).thenCompose(r -> completedFuture(!r));
    }

    @Override
    default ThrowableAsyncLongPredicate negateAsync() {
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r));
    }

    @Override
    default ThrowableAsyncLongPredicate negateAsync(Executor e) {
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> completedFuture(!r), e);
    }

    default ThrowableAsyncLongPredicate or(ThrowableAsyncLongPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenCompose(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : execute(t, o));
    }

    default ThrowableAsyncLongPredicate orAsync(ThrowableAsyncLongPredicate o) {
        ObjectUtils.requireNonNull(o);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : execute(t, o));
    }

    default ThrowableAsyncLongPredicate orAsync(ThrowableAsyncLongPredicate o, Executor e) {
        ObjectUtils.requireNonNull(o);
        ObjectUtils.requireNonNull(e);
        return t -> test(t).thenComposeAsync(r -> BooleanConstants.TRUE.equals(r) ? completedFuture(true) : execute(t, o), e);
    }

    static CompletableFuture<Boolean> execute(Long t, ThrowableAsyncLongPredicate p) throws IllegalArgumentException {
        try {
            return p.test(t);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}