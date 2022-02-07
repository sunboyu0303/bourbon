package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.source.*;
import org.springframework.core.CollectionFactory;
import org.springframework.core.ResolvableType;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 23:04
 */
class MapBinder extends AggregateBinder<Map<Object, Object>> {

    private static final Bindable<Map<String, String>> STRING_STRING_MAP = Bindable.mapOf(String.class, String.class);

    MapBinder(Binder.Context context) {
        super(context);
    }

    @Override
    protected boolean isAllowRecursiveBinding(ConfigurationPropertySource source) {
        return true;
    }

    @Override
    protected Object bindAggregate(ConfigurationPropertyName name, Bindable<?> target, AggregateElementBinder elementBinder) {
        Map<Object, Object> map = CollectionFactory.createMap(BooleanUtils.defaultIfFalse(target.getValue() == null, () -> target.getType().resolve(Object.class), Map.class), 0);
        Bindable<?> resolvedTarget = resolveTarget(target);
        boolean hasDescendants = hasDescendants(name);
        for (ConfigurationPropertySource source : getContext().getSources()) {
            if (!ConfigurationPropertyName.EMPTY.equals(name)) {
                ConfigurationProperty property = source.getConfigurationProperty(name);
                if (property != null && !hasDescendants) {
                    return getContext().getConverter().convert(property.getValue(), target);
                }
                source = source.filter(name::isAncestorOf);
            }
            new EntryBinder(name, resolvedTarget, elementBinder).bindEntries(source, map);
        }
        return BooleanUtils.defaultIfPredicate(map, m -> !m.isEmpty(), m -> m);
    }

    private boolean hasDescendants(ConfigurationPropertyName name) {
        for (ConfigurationPropertySource source : getContext().getSources()) {
            if (source.containsDescendantOf(name) == ConfigurationPropertyState.PRESENT) {
                return true;
            }
        }
        return false;
    }

    private Bindable<?> resolveTarget(Bindable<?> target) {
        return BooleanUtils.defaultIfFalse(Properties.class.isAssignableFrom(target.getType().resolve(Object.class)), STRING_STRING_MAP, target);
    }

    @Override
    protected Map<Object, Object> merge(Supplier<Map<Object, Object>> existing, Map<Object, Object> additional) {
        return ObjectUtils.defaultIfNullElseFunction(getExistingIfPossible(existing), m -> {
            try {
                m.putAll(additional);
                return copyIfPossible(m);
            } catch (UnsupportedOperationException e) {
                Map<Object, Object> result = createNewMap(additional.getClass(), m);
                result.putAll(additional);
                return result;
            }
        }, additional);
    }

    private Map<Object, Object> getExistingIfPossible(Supplier<Map<Object, Object>> existing) {
        try {
            return existing.get();
        } catch (Exception ex) {
            return null;
        }
    }

    private Map<Object, Object> copyIfPossible(Map<Object, Object> map) {
        try {
            return createNewMap(map.getClass(), map);
        } catch (Exception ex) {
            return map;
        }
    }

    private Map<Object, Object> createNewMap(Class<?> mapClass, Map<Object, Object> map) {
        Map<Object, Object> result = CollectionFactory.createMap(mapClass, map.size());
        result.putAll(map);
        return result;
    }

    private class EntryBinder {

        private final ConfigurationPropertyName root;

        private final AggregateElementBinder elementBinder;

        private final ResolvableType mapType;

        private final ResolvableType keyType;

        private final ResolvableType valueType;

        EntryBinder(ConfigurationPropertyName root, Bindable<?> target, AggregateElementBinder elementBinder) {
            this.root = root;
            this.elementBinder = elementBinder;
            this.mapType = target.getType().asMap();
            this.keyType = mapType.getGeneric(0);
            this.valueType = mapType.getGeneric(1);
        }

        void bindEntries(ConfigurationPropertySource source, Map<Object, Object> map) {
            if (source instanceof IterableConfigurationPropertySource) {
                for (ConfigurationPropertyName name : (IterableConfigurationPropertySource) source) {
                    Bindable<?> valueBindable = getValueBindable(name);
                    ConfigurationPropertyName entryName = getEntryName(source, name);
                    map.computeIfAbsent(getContext().getConverter().convert(getKeyName(entryName), keyType), k -> elementBinder.bind(entryName, valueBindable));
                }
            }
        }

        private Bindable<?> getValueBindable(ConfigurationPropertyName name) {
            return BooleanUtils.defaultSupplierIfFalse(!root.isParentOf(name) && isValueTreatedAsNestedMap(), () -> Bindable.of(mapType), () -> Bindable.of(valueType));
        }

        private ConfigurationPropertyName getEntryName(ConfigurationPropertySource source, ConfigurationPropertyName name) {
            return BooleanUtils.defaultSupplierIfFalse(Collection.class.isAssignableFrom(valueType.resolve(Object.class)) || valueType.isArray(), () -> chopNameAtNumericIndex(name),
                    () -> BooleanUtils.defaultIfFalse(!root.isParentOf(name) && (isValueTreatedAsNestedMap() || !isScalarValue(source, name)), () -> name.chop(root.getNumberOfElements() + 1), name)
            );
        }

        private ConfigurationPropertyName chopNameAtNumericIndex(ConfigurationPropertyName name) {
            for (int i = root.getNumberOfElements() + 1; i < name.getNumberOfElements(); i++) {
                if (name.isNumericIndex(i)) {
                    return name.chop(i);
                }
            }
            return name;
        }

        private boolean isValueTreatedAsNestedMap() {
            return Object.class.equals(valueType.resolve(Object.class));
        }

        private boolean isScalarValue(ConfigurationPropertySource source, ConfigurationPropertyName name) {
            Class<?> resolved = valueType.resolve(Object.class);
            if (!resolved.getName().startsWith("java.lang") && !resolved.isEnum()) {
                return false;
            }
            return ObjectUtils.defaultIfNullElseFunction(source.getConfigurationProperty(name), p -> getContext().getConverter().canConvert(
                    getContext().getPlaceholdersResolver().resolvePlaceholders(p.getValue()), valueType
            ), false);
        }

        private String getKeyName(ConfigurationPropertyName name) {
            StringBuilder result = new StringBuilder();
            for (int i = root.getNumberOfElements(); i < name.getNumberOfElements(); i++) {
                if (result.length() != 0) {
                    result.append(CharConstants.DOT);
                }
                result.append(name.getElement(i, ConfigurationPropertyName.Form.ORIGINAL));
            }
            return result.toString();
        }
    }
}