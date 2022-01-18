package com.github.bourbon.base.utils.function.async.throwable;

import java.util.concurrent.CompletableFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 16:00
 */
@FunctionalInterface
public interface ThrowableAsyncIntSupplier extends ThrowableAsyncSupplier<Integer> {

    static CompletableFuture<Integer> execute(ThrowableAsyncIntSupplier s) throws IllegalArgumentException {
        try {
            return s.get();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}