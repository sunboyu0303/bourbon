package com.github.bourbon.base.utils.concurrent.atomic;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 00:26
 */
public class AtomicLazy<T> implements Supplier<T> {
    private final Supplier<T> supplier;

    private final AtomicReference<T> reference = new AtomicReference<>();

    private AtomicLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public <S> AtomicLazy<S> map(Function<? super T, ? extends S> f) {
        return of(() -> f.apply(get()));
    }

    public <S> AtomicLazy<S> flatMap(Function<? super T, AtomicLazy<? extends S>> f) {
        return of(() -> f.apply(get()).get());
    }

    public static <T> AtomicLazy<T> of(Supplier<T> supplier) {
        return new AtomicLazy<>(supplier);
    }

    @Override
    public T get() {
        return ObjectUtils.defaultSupplierIfNull(reference.get(), () -> {
            reference.compareAndSet(null, supplier.get());
            return reference.get();
        });
    }
}