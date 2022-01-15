package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 16:06
 */
public final class BindResult<T> {

    private static final BindResult<?> UNBOUND = new BindResult<>(null);

    private final T value;

    private BindResult(T value) {
        this.value = value;
    }

    public T get() throws NoSuchElementException {
        return ObjectUtils.requireNonNull(value, () -> new NoSuchElementException("No value bound"));
    }

    public boolean isBound() {
        return value != null;
    }

    public void ifBound(Consumer<? super T> consumer) {
        Assert.notNull(consumer, "Consumer must not be null");
        if (value != null) {
            consumer.accept(value);
        }
    }

    public <U> BindResult<U> map(Function<T, U> mapper) {
        Assert.notNull(mapper, "Mapper must not be null");
        return of(ObjectUtils.defaultIfNull(value, mapper));
    }

    public T orElse(T other) {
        return ObjectUtils.defaultIfNull(value, other);
    }

    public T orElseGet(Supplier<T> other) {
        return ObjectUtils.defaultSupplierIfNull(value, other);
    }

    public <X extends Throwable> T orElseThrow(ThrowableSupplier<X> exceptionSupplier) throws X {
        return ObjectUtils.requireNonNull(value, exceptionSupplier);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return ObjectUtils.nullSafeEquals(value, ((BindResult<?>) obj).value);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(value);
    }

    @SuppressWarnings("unchecked")
    static <T> BindResult<T> of(T value) {
        return ObjectUtils.defaultSupplierIfNull(value, BindResult::new, () -> (BindResult<T>) UNBOUND);
    }
}