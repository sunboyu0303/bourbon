package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.core.CollectionFactory;
import org.springframework.core.ResolvableType;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 23:41
 */
class CollectionBinder extends IndexedElementsBinder<Collection<Object>> {

    CollectionBinder(Binder.Context context) {
        super(context);
    }

    @Override
    protected Object bindAggregate(ConfigurationPropertyName name, Bindable<?> target, AggregateElementBinder binder) {

        Class<?> collectionType = BooleanUtils.defaultSupplierIfFalse(target.getValue() == null, () -> target.getType().resolve(Object.class), () -> List.class);

        ResolvableType aggregateType = ResolvableType.forClassWithGenerics(List.class, target.getType().asCollection().getGenerics());
        ResolvableType elementType = target.getType().asCollection().getGeneric();

        IndexedCollectionSupplier result = new IndexedCollectionSupplier(() -> CollectionFactory.createCollection(collectionType, elementType.resolve(), 0));
        bindIndexed(name, target, binder, aggregateType, elementType, result);

        return BooleanUtils.defaultIfFalse(result.wasSupplied(), result);
    }

    @Override
    protected Collection<Object> merge(Supplier<Collection<Object>> existing, Collection<Object> additional) {
        return ObjectUtils.defaultIfNull(getExistingIfPossible(existing), c -> {
            try {
                c.clear();
                c.addAll(additional);
                return copyIfPossible(c);
            } catch (UnsupportedOperationException ex) {
                return createNewCollection(additional);
            }
        }, additional);
    }

    private Collection<Object> getExistingIfPossible(Supplier<Collection<Object>> s) {
        try {
            return s.get();
        } catch (Exception ex) {
            return null;
        }
    }

    private Collection<Object> copyIfPossible(Collection<Object> c) {
        try {
            return createNewCollection(c);
        } catch (Exception ex) {
            return c;
        }
    }

    private Collection<Object> createNewCollection(Collection<Object> c) {
        Collection<Object> result = CollectionFactory.createCollection(c.getClass(), c.size());
        result.addAll(c);
        return result;
    }
}