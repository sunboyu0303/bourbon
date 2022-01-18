package com.github.bourbon.base.utils.function;

import com.github.bourbon.base.utils.ObjectUtils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 00:19
 */
public class SafeLazy<T> implements Supplier<T> {

    private final Supplier<T> supplier;

    private AtomicReference<T> value = new AtomicReference<>();

    private SafeLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public <S> SafeLazy<S> map(Function<? super T, ? extends S> f) {
        return of(() -> f.apply(get()));
    }

    public <S> SafeLazy<S> flatMap(Function<? super T, SafeLazy<? extends S>> f) {
        return of(() -> f.apply(get()).get());
    }

    public static <T> SafeLazy<T> of(Supplier<T> supplier) {
        return new SafeLazy<>(supplier);
    }

    @Override
    public T get() {
        T t = value.get();
        if (ObjectUtils.isNull(t)) {
            synchronized (this) {
                t = value.get();
                if (ObjectUtils.isNull(t)) {
                    t = supplier.get();
                    value.set(t);
                }
            }
        }
        return t;
    }
}