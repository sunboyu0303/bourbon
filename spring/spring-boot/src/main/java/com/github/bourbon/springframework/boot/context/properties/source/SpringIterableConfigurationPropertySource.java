package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.origin.OriginLookup;
import com.github.bourbon.springframework.boot.origin.PropertySourceOrigin;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 19:06
 */
class SpringIterableConfigurationPropertySource extends SpringConfigurationPropertySource implements IterableConfigurationPropertySource, CachingConfigurationPropertySource {

    private final BiPredicate<ConfigurationPropertyName, ConfigurationPropertyName> ancestorOfCheck;

    private final SoftReferenceConfigurationPropertyCache<Mappings> cache;

    private volatile ConfigurationPropertyName[] configurationPropertyNames;

    SpringIterableConfigurationPropertySource(EnumerablePropertySource<?> propertySource, PropertyMapper... mappers) {
        super(propertySource, mappers);
        assertEnumerablePropertySource();
        this.ancestorOfCheck = getAncestorOfCheck(mappers);
        this.cache = new SoftReferenceConfigurationPropertyCache<>(isImmutablePropertySource());
    }

    private BiPredicate<ConfigurationPropertyName, ConfigurationPropertyName> getAncestorOfCheck(PropertyMapper[] mappers) {
        BiPredicate<ConfigurationPropertyName, ConfigurationPropertyName> ancestorOfCheck = mappers[0].getAncestorOfCheck();
        for (int i = 1; i < mappers.length; i++) {
            ancestorOfCheck = ancestorOfCheck.or(mappers[i].getAncestorOfCheck());
        }
        return ancestorOfCheck;
    }

    private void assertEnumerablePropertySource() {
        if (getPropertySource() instanceof MapPropertySource) {
            try {
                ((MapPropertySource) getPropertySource()).getSource().size();
            } catch (UnsupportedOperationException ex) {
                throw new IllegalArgumentException("PropertySource must be fully enumerable");
            }
        }
    }

    @Override
    public ConfigurationPropertyCaching getCaching() {
        return cache;
    }

    @Override
    public ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name) {
        if (name == null) {
            return null;
        }
        ConfigurationProperty configurationProperty = super.getConfigurationProperty(name);
        if (configurationProperty != null) {
            return configurationProperty;
        }
        for (String candidate : getMappings().getMapped(name)) {
            Object value = getPropertySource().getProperty(candidate);
            if (value != null) {
                return ConfigurationProperty.of(this, name, value, PropertySourceOrigin.get(getPropertySource(), candidate));
            }
        }
        return null;
    }

    @Override
    public Stream<ConfigurationPropertyName> stream() {
        return Arrays.stream(getConfigurationPropertyNames()).filter(Objects::nonNull);
    }

    @Override
    public Iterator<ConfigurationPropertyName> iterator() {
        return new ConfigurationPropertyNamesIterator(getConfigurationPropertyNames());
    }

    @Override
    public ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        ConfigurationPropertyState result = super.containsDescendantOf(name);
        if (result != ConfigurationPropertyState.UNKNOWN) {
            return result;
        }
        if (ancestorOfCheck == PropertyMapper.DEFAULT_ANCESTOR_OF_CHECK) {
            return getMappings().containsDescendantOf(name, ancestorOfCheck);
        }
        ConfigurationPropertyName[] candidates = getConfigurationPropertyNames();
        for (ConfigurationPropertyName candidate : candidates) {
            if (candidate != null && ancestorOfCheck.test(name, candidate)) {
                return ConfigurationPropertyState.PRESENT;
            }
        }
        return ConfigurationPropertyState.ABSENT;
    }

    private ConfigurationPropertyName[] getConfigurationPropertyNames() {
        if (!isImmutablePropertySource()) {
            return getMappings().getConfigurationPropertyNames(getPropertySource().getPropertyNames());
        }
        ConfigurationPropertyName[] configurationPropertyNames = this.configurationPropertyNames;
        if (configurationPropertyNames == null) {
            configurationPropertyNames = getMappings().getConfigurationPropertyNames(getPropertySource().getPropertyNames());
            this.configurationPropertyNames = configurationPropertyNames;
        }
        return configurationPropertyNames;
    }

    private Mappings getMappings() {
        return cache.get(this::createMappings, this::updateMappings);
    }

    private Mappings createMappings() {
        return new Mappings(getMappers(), isImmutablePropertySource(), ancestorOfCheck == PropertyMapper.DEFAULT_ANCESTOR_OF_CHECK);
    }

    private Mappings updateMappings(Mappings mappings) {
        mappings.updateMappings(getPropertySource()::getPropertyNames);
        return mappings;
    }

    private boolean isImmutablePropertySource() {
        EnumerablePropertySource<?> source = getPropertySource();
        if (source instanceof OriginLookup) {
            return ((OriginLookup<?>) source).isImmutable();
        }
        if (StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME.equals(source.getName())) {
            return source.getSource() == System.getenv();
        }
        return false;
    }

    @Override
    protected EnumerablePropertySource<?> getPropertySource() {
        return (EnumerablePropertySource<?>) super.getPropertySource();
    }

    private static class Mappings {

        private static final ConfigurationPropertyName[] EMPTY_NAMES_ARRAY = {};

        private final PropertyMapper[] mappers;

        private final boolean immutable;

        private final boolean trackDescendants;

        private volatile Map<ConfigurationPropertyName, Set<String>> mappings;

        private volatile Map<String, ConfigurationPropertyName> reverseMappings;

        private volatile Map<ConfigurationPropertyName, Set<ConfigurationPropertyName>> descendants;

        private volatile ConfigurationPropertyName[] configurationPropertyNames;

        private volatile String[] lastUpdated;

        Mappings(PropertyMapper[] mappers, boolean immutable, boolean trackDescendants) {
            this.mappers = mappers;
            this.immutable = immutable;
            this.trackDescendants = trackDescendants;
        }

        void updateMappings(Supplier<String[]> propertyNames) {
            if (mappings == null || !immutable) {
                int count = 0;
                while (true) {
                    try {
                        updateMappings(propertyNames.get());
                        return;
                    } catch (ConcurrentModificationException ex) {
                        if (count++ > 10) {
                            throw ex;
                        }
                    }
                }
            }
        }

        private void updateMappings(String[] propertyNames) {
            String[] lastUpdated = this.lastUpdated;
            if (lastUpdated != null && Arrays.equals(lastUpdated, propertyNames)) {
                return;
            }
            int size = propertyNames.length;
            Map<ConfigurationPropertyName, Set<String>> mappings = cloneOrCreate(this.mappings, size);
            Map<String, ConfigurationPropertyName> reverseMappings = cloneOrCreate(this.reverseMappings, size);
            Map<ConfigurationPropertyName, Set<ConfigurationPropertyName>> descendants = cloneOrCreate(this.descendants, size);

            for (PropertyMapper propertyMapper : mappers) {
                for (String propertyName : propertyNames) {
                    if (!reverseMappings.containsKey(propertyName)) {
                        ConfigurationPropertyName configurationPropertyName = propertyMapper.map(propertyName);
                        if (configurationPropertyName != null && !configurationPropertyName.isEmpty()) {
                            add(mappings, configurationPropertyName, propertyName);
                            reverseMappings.put(propertyName, configurationPropertyName);
                            if (trackDescendants) {
                                addParents(descendants, configurationPropertyName);
                            }
                        }
                    }
                }
            }
            this.mappings = mappings;
            this.reverseMappings = reverseMappings;
            this.descendants = descendants;
            this.lastUpdated = BooleanUtils.defaultIfFalse(immutable, (String[]) null, propertyNames);
            this.configurationPropertyNames = BooleanUtils.defaultIfFalse(immutable, () -> reverseMappings.values().toArray(new ConfigurationPropertyName[0]));
        }

        private <K, V> Map<K, V> cloneOrCreate(Map<K, V> source, int size) {
            return ObjectUtils.defaultSupplierIfNull(source, LinkedHashMap::new, () -> new LinkedHashMap<>(size));
        }

        private void addParents(Map<ConfigurationPropertyName, Set<ConfigurationPropertyName>> descendants, ConfigurationPropertyName name) {
            ConfigurationPropertyName parent = name;
            while (!parent.isEmpty()) {
                add(descendants, parent, name);
                parent = parent.getParent();
            }
        }

        private <K, T> void add(Map<K, Set<T>> map, K key, T value) {
            map.computeIfAbsent(key, k -> new HashSet<>()).add(value);
        }

        Set<String> getMapped(ConfigurationPropertyName configurationPropertyName) {
            return mappings.getOrDefault(configurationPropertyName, Collections.emptySet());
        }

        ConfigurationPropertyName[] getConfigurationPropertyNames(String[] propertyNames) {
            ConfigurationPropertyName[] names = configurationPropertyNames;
            if (names != null) {
                return names;
            }
            Map<String, ConfigurationPropertyName> reverseMappings = this.reverseMappings;
            if (MapUtils.isEmpty(reverseMappings)) {
                return EMPTY_NAMES_ARRAY;
            }
            names = new ConfigurationPropertyName[propertyNames.length];
            for (int i = 0; i < propertyNames.length; i++) {
                names[i] = reverseMappings.get(propertyNames[i]);
            }
            return names;
        }

        ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name, BiPredicate<ConfigurationPropertyName, ConfigurationPropertyName> ancestorOfCheck) {
            if (name.isEmpty() && !descendants.isEmpty()) {
                return ConfigurationPropertyState.PRESENT;
            }
            Set<ConfigurationPropertyName> candidates = descendants.getOrDefault(name, Collections.emptySet());
            for (ConfigurationPropertyName candidate : candidates) {
                if (ancestorOfCheck.test(name, candidate)) {
                    return ConfigurationPropertyState.PRESENT;
                }
            }
            return ConfigurationPropertyState.ABSENT;
        }
    }

    private static class ConfigurationPropertyNamesIterator implements Iterator<ConfigurationPropertyName> {

        private final ConfigurationPropertyName[] names;

        private int index = 0;

        ConfigurationPropertyNamesIterator(ConfigurationPropertyName[] names) {
            this.names = names;
        }

        @Override
        public boolean hasNext() {
            skipNulls();
            return index < names.length;
        }

        @Override
        public ConfigurationPropertyName next() {
            skipNulls();
            if (index >= names.length) {
                throw new NoSuchElementException();
            }
            return names[index++];
        }

        private void skipNulls() {
            while (index < names.length) {
                if (names[index] != null) {
                    return;
                }
                index++;
            }
        }
    }
}