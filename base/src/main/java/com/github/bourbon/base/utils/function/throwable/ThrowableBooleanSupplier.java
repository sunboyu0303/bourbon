package com.github.bourbon.base.utils.function.throwable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:06
 */
@FunctionalInterface
public interface ThrowableBooleanSupplier extends ThrowableSupplier<Boolean> {

    static Boolean execute(ThrowableBooleanSupplier supplier) throws IllegalArgumentException {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}