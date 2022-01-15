package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 00:05
 */
class ArrayBinder extends IndexedElementsBinder<Object> {

    ArrayBinder(Binder.Context context) {
        super(context);
    }

    @Override
    protected Object bindAggregate(ConfigurationPropertyName name, Bindable<?> target, AggregateElementBinder elementBinder) {

        IndexedCollectionSupplier result = new IndexedCollectionSupplier(ArrayList::new);

        ResolvableType aggregateType = target.getType();
        ResolvableType elementType = aggregateType.getComponentType();
        bindIndexed(name, target, elementBinder, aggregateType, elementType, result);

        return BooleanUtils.defaultIfFalse(result.wasSupplied(), () -> {
            List<Object> list = (List<Object>) result.get();
            Object array = Array.newInstance(elementType.resolve(), list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, list.get(i));
            }
            return array;
        });
    }

    @Override
    protected Object merge(Supplier<Object> existing, Object additional) {
        return additional;
    }
}