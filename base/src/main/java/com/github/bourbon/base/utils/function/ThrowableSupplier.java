package com.github.bourbon.base.utils.function;

import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 18:03
 */
public interface ThrowableSupplier<X extends Throwable> extends Supplier<X> {
}