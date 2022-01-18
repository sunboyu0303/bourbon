package com.github.bourbon.base.utils.function;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 16:30
 */
public class Lazy<T> implements Supplier<T> {

    private final Supplier<T> supplier;

    private T value;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public <S> Lazy<S> map(Function<? super T, ? extends S> f) {
        return of(() -> f.apply(get()));
    }

    public <S> Lazy<S> flatMap(Function<? super T, Lazy<? extends S>> f) {
        return of(() -> f.apply(get()).get());
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    @Override
    public T get() {
        if (ObjectUtils.isNull(value)) {
            value = supplier.get();
        }
        return value;
    }
}