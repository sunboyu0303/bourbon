package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 15:49
 */
class PrefixedConfigurationPropertySource implements ConfigurationPropertySource {

    private final ConfigurationPropertySource source;
    private final ConfigurationPropertyName prefix;

    PrefixedConfigurationPropertySource(ConfigurationPropertySource source, String prefix) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(prefix, "Prefix must not be empty");
        this.source = source;
        this.prefix = ConfigurationPropertyName.of(prefix);
    }

    protected final ConfigurationPropertyName getPrefix() {
        return prefix;
    }

    @Override
    public ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name) {
        return ObjectUtils.defaultIfNullElseFunction(source.getConfigurationProperty(getPrefixedName(name)), p -> ConfigurationProperty.of(p.getSource(), name, p.getValue(), p.getOrigin()));
    }

    private ConfigurationPropertyName getPrefixedName(ConfigurationPropertyName name) {
        return prefix.append(name);
    }

    @Override
    public ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        return source.containsDescendantOf(getPrefixedName(name));
    }

    @Override
    public Object getUnderlyingSource() {
        return source.getUnderlyingSource();
    }

    protected ConfigurationPropertySource getSource() {
        return source;
    }
}