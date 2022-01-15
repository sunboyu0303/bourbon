package com.github.bourbon.springframework.boot.context.properties.source;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 00:09
 */
class FilteredIterableConfigurationPropertiesSource extends FilteredConfigurationPropertiesSource implements IterableConfigurationPropertySource {

    FilteredIterableConfigurationPropertiesSource(IterableConfigurationPropertySource source, Predicate<ConfigurationPropertyName> filter) {
        super(source, filter);
    }

    @Override
    public Stream<ConfigurationPropertyName> stream() {
        return getSource().stream().filter(getFilter());
    }

    @Override
    protected IterableConfigurationPropertySource getSource() {
        return (IterableConfigurationPropertySource) super.getSource();
    }

    @Override
    public ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        return ConfigurationPropertyState.search(this, name::isAncestorOf);
    }
}