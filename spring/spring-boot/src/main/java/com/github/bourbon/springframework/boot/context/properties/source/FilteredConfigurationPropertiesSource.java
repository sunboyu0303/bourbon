package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.function.Predicate;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 15:39
 */
class FilteredConfigurationPropertiesSource implements ConfigurationPropertySource {

    private final ConfigurationPropertySource source;

    private final Predicate<ConfigurationPropertyName> filter;

    FilteredConfigurationPropertiesSource(ConfigurationPropertySource source, Predicate<ConfigurationPropertyName> filter) {
        this.source = ObjectUtils.requireNonNull(source, "Source must not be null");
        this.filter = ObjectUtils.requireNonNull(filter, "Filter must not be null");
    }

    @Override
    public ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name) {
        return BooleanUtils.defaultIfFalse(getFilter().test(name), () -> getSource().getConfigurationProperty(name));
    }

    @Override
    public ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        ConfigurationPropertyState result = source.containsDescendantOf(name);
        return BooleanUtils.defaultIfFalse(result == ConfigurationPropertyState.PRESENT, ConfigurationPropertyState.UNKNOWN, result);
    }

    @Override
    public Object getUnderlyingSource() {
        return source.getUnderlyingSource();
    }

    protected ConfigurationPropertySource getSource() {
        return source;
    }

    protected Predicate<ConfigurationPropertyName> getFilter() {
        return filter;
    }

    @Override
    public String toString() {
        return source.toString() + " (filtered)";
    }
}