package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertySource;

import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 14:42
 */
abstract class AggregateBinder<T> {

    private final Binder.Context context;

    AggregateBinder(Binder.Context context) {
        this.context = context;
    }

    protected abstract boolean isAllowRecursiveBinding(ConfigurationPropertySource source);

    @SuppressWarnings("unchecked")
    final Object bind(ConfigurationPropertyName name, Bindable<?> target, AggregateElementBinder elementBinder) {
        Object result = bindAggregate(name, target, elementBinder);
        Supplier<?> value = target.getValue();
        return BooleanUtils.defaultIfFalse(ObjectUtils.nonNull(result) && ObjectUtils.nonNull(value),
                () -> merge((Supplier<T>) value, (T) result), result
        );
    }

    protected abstract Object bindAggregate(ConfigurationPropertyName name, Bindable<?> target, AggregateElementBinder elementBinder);

    protected abstract T merge(Supplier<T> existing, T additional);

    final Binder.Context getContext() {
        return context;
    }

    static class AggregateSupplier<T> implements Supplier<T> {

        private final Supplier<T> supplier;

        private T supplied;

        AggregateSupplier(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            return ObjectUtils.defaultSupplierIfNull(supplied, () -> {
                supplied = supplier.get();
                return supplied;
            });
        }

        boolean wasSupplied() {
            return ObjectUtils.nonNull(supplied);
        }
    }
}