package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.origin.PropertySourceOrigin;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import java.util.Map;
import java.util.Random;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 15:33
 */
class SpringConfigurationPropertySource implements ConfigurationPropertySource {

    private static final PropertyMapper[] DEFAULT_MAPPERS = {DefaultPropertyMapper.INSTANCE};

    private static final PropertyMapper[] SYSTEM_ENVIRONMENT_MAPPERS = {SystemEnvironmentPropertyMapper.INSTANCE, DefaultPropertyMapper.INSTANCE};

    private final PropertySource<?> propertySource;

    private final PropertyMapper[] mappers;

    SpringConfigurationPropertySource(PropertySource<?> propertySource, PropertyMapper... mappers) {
        Assert.notNull(propertySource, "PropertySource must not be null");
        Assert.notEmpty(mappers, "Mappers must contain at least one item");
        this.propertySource = propertySource;
        this.mappers = mappers;
    }

    @Override
    public ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name) {
        return ObjectUtils.defaultIfNullElseFunction(name, n -> {
            for (PropertyMapper mapper : mappers) {
                try {
                    for (String candidate : mapper.map(n)) {
                        Object value = getPropertySource().getProperty(candidate);
                        if (value != null) {
                            return ConfigurationProperty.of(this, n, value, PropertySourceOrigin.get(propertySource, candidate));
                        }
                    }
                } catch (Exception ex) {
                    // ignore
                }
            }
            return null;
        });
    }

    @Override
    public ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        PropertySource<?> source = getPropertySource();
        return BooleanUtils.defaultSupplierIfFalse(source.getSource() instanceof Random,
                () -> containsDescendantOfForRandom("random", name),
                () -> BooleanUtils.defaultIfFalse(source.getSource() instanceof PropertySource<?> && ((PropertySource<?>) source.getSource()).getSource() instanceof Random,
                        () -> containsDescendantOfForRandom(source.getName(), name), ConfigurationPropertyState.UNKNOWN
                )
        );
    }

    private static ConfigurationPropertyState containsDescendantOfForRandom(String prefix, ConfigurationPropertyName name) {
        return BooleanUtils.defaultIfFalse(name.getNumberOfElements() > 1 && name.getElement(0, ConfigurationPropertyName.Form.DASHED).equals(prefix),
                ConfigurationPropertyState.PRESENT, ConfigurationPropertyState.ABSENT
        );
    }

    @Override
    public Object getUnderlyingSource() {
        return propertySource;
    }

    protected PropertySource<?> getPropertySource() {
        return propertySource;
    }

    protected final PropertyMapper[] getMappers() {
        return mappers;
    }

    @Override
    public String toString() {
        return propertySource.toString();
    }

    static SpringConfigurationPropertySource from(PropertySource<?> source) {
        Assert.notNull(source, "Source must not be null");
        PropertyMapper[] mappers = getPropertyMappers(source);
        return BooleanUtils.defaultSupplierIfFalse(isFullEnumerable(source),
                () -> new SpringIterableConfigurationPropertySource((EnumerablePropertySource<?>) source, mappers),
                () -> new SpringConfigurationPropertySource(source, mappers)
        );
    }

    private static PropertyMapper[] getPropertyMappers(PropertySource<?> source) {
        return BooleanUtils.defaultIfFalse(source instanceof SystemEnvironmentPropertySource && hasSystemEnvironmentName(source), SYSTEM_ENVIRONMENT_MAPPERS, DEFAULT_MAPPERS);
    }

    private static boolean hasSystemEnvironmentName(PropertySource<?> source) {
        String name = source.getName();
        return StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME.equals(name) || name.endsWith(StringConstants.HYPHEN + StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);
    }

    private static boolean isFullEnumerable(PropertySource<?> source) {
        PropertySource<?> rootSource = getRootSource(source);
        if (rootSource.getSource() instanceof Map) {
            try {
                ((Map<?, ?>) rootSource.getSource()).size();
            } catch (UnsupportedOperationException ex) {
                return false;
            }
        }
        return source instanceof EnumerablePropertySource;
    }

    private static PropertySource<?> getRootSource(PropertySource<?> source) {
        while (source.getSource() instanceof PropertySource) {
            source = (PropertySource<?>) source.getSource();
        }
        return source;
    }
}