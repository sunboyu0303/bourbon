package com.github.bourbon.springframework.boot.context.properties.source;

import java.util.stream.Stream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 00:12
 */
class PrefixedIterableConfigurationPropertySource extends PrefixedConfigurationPropertySource implements IterableConfigurationPropertySource {

    PrefixedIterableConfigurationPropertySource(IterableConfigurationPropertySource source, String prefix) {
        super(source, prefix);
    }

    @Override
    public Stream<ConfigurationPropertyName> stream() {
        return getSource().stream().map(this::stripPrefix);
    }

    private ConfigurationPropertyName stripPrefix(ConfigurationPropertyName name) {
        return getPrefix().isAncestorOf(name) ? name.subName(getPrefix().getNumberOfElements()) : name;
    }

    @Override
    protected IterableConfigurationPropertySource getSource() {
        return (IterableConfigurationPropertySource) super.getSource();
    }
}