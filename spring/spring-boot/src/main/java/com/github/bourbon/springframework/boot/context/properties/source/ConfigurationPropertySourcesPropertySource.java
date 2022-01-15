package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.origin.Origin;
import com.github.bourbon.springframework.boot.origin.OriginLookup;
import org.springframework.core.env.PropertySource;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 15:36
 */
class ConfigurationPropertySourcesPropertySource extends PropertySource<Iterable<ConfigurationPropertySource>> implements OriginLookup<String> {

    ConfigurationPropertySourcesPropertySource(String name, Iterable<ConfigurationPropertySource> source) {
        super(name, source);
    }

    @Override
    public boolean containsProperty(String name) {
        return findConfigurationProperty(name) != null;
    }

    @Override
    public Object getProperty(String name) {
        return ObjectUtils.defaultIfNull(findConfigurationProperty(name), ConfigurationProperty::getValue);
    }

    @Override
    public Origin getOrigin(String name) {
        return Origin.from(findConfigurationProperty(name));
    }

    private ConfigurationProperty findConfigurationProperty(String name) {
        try {
            return findConfigurationProperty(ConfigurationPropertyName.of(name, true));
        } catch (Exception ex) {
            return null;
        }
    }

    ConfigurationProperty findConfigurationProperty(ConfigurationPropertyName name) {
        if (name == null) {
            return null;
        }
        for (ConfigurationPropertySource configurationPropertySource : getSource()) {
            ConfigurationProperty configurationProperty = configurationPropertySource.getConfigurationProperty(name);
            if (configurationProperty != null) {
                return configurationProperty;
            }
        }
        return null;
    }
}