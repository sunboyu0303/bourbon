package com.github.bourbon.base.utils.function.throwable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/19 16:27
 */
@FunctionalInterface
public interface ThrowableIntSupplier extends ThrowableSupplier<Integer> {

    static Integer execute(ThrowableIntSupplier supplier) throws IllegalArgumentException {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}