package com.github.bourbon.base.utils.function.throwable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:39
 */
@FunctionalInterface
public interface ThrowableLongSupplier extends ThrowableSupplier<Long> {

    static Long execute(ThrowableLongSupplier supplier) throws IllegalArgumentException {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}