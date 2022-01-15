package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationProperty;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertySource;
import com.github.bourbon.springframework.boot.context.properties.source.IterableConfigurationPropertySource;
import org.springframework.core.ResolvableType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 23:44
 */
abstract class IndexedElementsBinder<T> extends AggregateBinder<T> {

    private static final String INDEX_ZERO = StringConstants.LEFT_BRACKETS_ZERO_RIGHT_BRACKETS;

    IndexedElementsBinder(Binder.Context context) {
        super(context);
    }

    @Override
    protected boolean isAllowRecursiveBinding(ConfigurationPropertySource source) {
        return ObjectUtils.isNull(source) || source instanceof IterableConfigurationPropertySource;
    }

    protected final void bindIndexed(ConfigurationPropertyName name, Bindable<?> target, AggregateElementBinder elementBinder, ResolvableType aggregateType, ResolvableType elementType, IndexedCollectionSupplier result) {
        for (ConfigurationPropertySource source : getContext().getSources()) {
            bindIndexed(source, name, target, elementBinder, result, aggregateType, elementType);
            if (result.wasSupplied() && result.get() != null) {
                return;
            }
        }
    }

    private void bindIndexed(ConfigurationPropertySource source, ConfigurationPropertyName root, Bindable<?> target, AggregateElementBinder elementBinder, IndexedCollectionSupplier collection, ResolvableType aggregateType, ResolvableType elementType) {
        ConfigurationProperty property = source.getConfigurationProperty(root);
        if (property != null) {
            getContext().setConfigurationProperty(property);
            bindValue(target, collection.get(), aggregateType, elementType, property.getValue());
        } else {
            bindIndexed(source, root, elementBinder, collection, elementType);
        }
    }

    private void bindValue(Bindable<?> target, Collection<Object> collection, ResolvableType aggregateType, ResolvableType elementType, Object value) {
        if (value == null || value instanceof CharSequence && ((CharSequence) value).length() == 0) {
            return;
        }
        collection.addAll(convert(convert(value, aggregateType, target.getAnnotations()), ResolvableType.forClassWithGenerics(collection.getClass(), elementType)));
    }

    private void bindIndexed(ConfigurationPropertySource source, ConfigurationPropertyName root, AggregateElementBinder elementBinder, IndexedCollectionSupplier collection, ResolvableType elementType) {
        MultiValueMap<String, ConfigurationPropertyName> knownIndexedChildren = getKnownIndexedChildren(source, root);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final int num = i;
            ConfigurationPropertyName name = root.append(BooleanUtils.defaultIfFalse(num != 0, () -> StringConstants.LEFT_BRACKETS + num + StringConstants.RIGHT_BRACKETS, INDEX_ZERO));
            Object value = elementBinder.bind(name, Bindable.of(elementType), source);
            if (value == null) {
                break;
            }
            knownIndexedChildren.remove(name.getLastElement(ConfigurationPropertyName.Form.UNIFORM));
            collection.get().add(value);
        }
        assertNoUnboundChildren(source, knownIndexedChildren);
    }

    private MultiValueMap<String, ConfigurationPropertyName> getKnownIndexedChildren(ConfigurationPropertySource source, ConfigurationPropertyName root) {
        MultiValueMap<String, ConfigurationPropertyName> children = new LinkedMultiValueMap<>();
        return BooleanUtils.defaultIfFalse(source instanceof IterableConfigurationPropertySource, () -> {
            for (ConfigurationPropertyName name : (IterableConfigurationPropertySource) source.filter(root::isAncestorOf)) {
                ConfigurationPropertyName choppedName = name.chop(root.getNumberOfElements() + 1);
                if (choppedName.isLastElementIndexed()) {
                    children.add(choppedName.getLastElement(ConfigurationPropertyName.Form.UNIFORM), name);
                }
            }
            return children;
        }, children);
    }

    private void assertNoUnboundChildren(ConfigurationPropertySource source, MultiValueMap<String, ConfigurationPropertyName> children) {
        Assert.isEmpty(children, () -> new UnboundConfigurationPropertiesException(
                children.values().stream().flatMap(List::stream).map(source::getConfigurationProperty).collect(Collectors.toCollection(TreeSet::new))
        ));
    }

    private <C> C convert(Object value, ResolvableType type, Annotation... annotations) {
        return getContext().getConverter().convert(getContext().getPlaceholdersResolver().resolvePlaceholders(value), type, annotations);
    }

    static class IndexedCollectionSupplier extends AggregateSupplier<Collection<Object>> {

        IndexedCollectionSupplier(Supplier<Collection<Object>> supplier) {
            super(supplier);
        }
    }
}