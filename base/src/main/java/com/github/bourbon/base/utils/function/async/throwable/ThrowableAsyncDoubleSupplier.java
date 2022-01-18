package com.github.bourbon.base.utils.function.async.throwable;

import java.util.concurrent.CompletableFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 11:20
 */
@FunctionalInterface
public interface ThrowableAsyncDoubleSupplier extends ThrowableAsyncSupplier<Double> {

    static CompletableFuture<Double> execute(ThrowableAsyncDoubleSupplier s) throws IllegalArgumentException {
        try {
            return s.get();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}