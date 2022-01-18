package com.github.bourbon.base.utils.function.throwable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 17:45
 */
@FunctionalInterface
public interface ThrowableSupplier<T> {

    T get() throws Exception;

    static <T> T execute(ThrowableSupplier<T> supplier) throws IllegalArgumentException {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}