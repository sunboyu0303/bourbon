package com.github.bourbon.base.utils.function.async.throwable;

import java.util.concurrent.CompletableFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/20 10:38
 */
@FunctionalInterface
public interface ThrowableAsyncAction {

    CompletableFuture<Void> execute() throws Exception;

    static CompletableFuture<Void> execute(ThrowableAsyncAction action) throws IllegalArgumentException {
        try {
            return action.execute();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}