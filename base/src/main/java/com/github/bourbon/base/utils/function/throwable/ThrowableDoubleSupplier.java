package com.github.bourbon.base.utils.function.throwable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:25
 */
@FunctionalInterface
public interface ThrowableDoubleSupplier extends ThrowableSupplier<Double> {

    static Double execute(ThrowableDoubleSupplier supplier) throws IllegalArgumentException {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}