package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.core.env.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 00:15
 */
class ConfigurationPropertySourcesPropertyResolver extends AbstractPropertyResolver {

    private final MutablePropertySources propertySources;

    private final DefaultResolver defaultResolver;

    ConfigurationPropertySourcesPropertyResolver(MutablePropertySources propertySources) {
        this.propertySources = propertySources;
        this.defaultResolver = new DefaultResolver(propertySources);
    }

    @Override
    public boolean containsProperty(String key) {
        ConfigurationPropertySourcesPropertySource attached = getAttached();
        if (attached != null) {
            ConfigurationPropertyName name = ConfigurationPropertyName.of(key, true);
            if (name != null) {
                try {
                    return attached.findConfigurationProperty(name) != null;
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return defaultResolver.containsProperty(key);
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, String.class, true);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetValueType) {
        return getProperty(key, targetValueType, true);
    }

    @Override
    protected String getPropertyAsRawString(String key) {
        return getProperty(key, String.class, false);
    }

    private <T> T getProperty(String key, Class<T> targetValueType, boolean resolveNestedPlaceholders) {
        return ObjectUtils.defaultIfNullElseFunction(findPropertyValue(key), v -> {
            if (resolveNestedPlaceholders && v instanceof String) {
                v = resolveNestedPlaceholders((String) v);
            }
            return convertValueIfNecessary(v, targetValueType);
        });
    }

    private Object findPropertyValue(String key) {
        ConfigurationPropertySourcesPropertySource attached = getAttached();
        if (attached != null) {
            ConfigurationPropertyName name = ConfigurationPropertyName.of(key, true);
            if (name != null) {
                try {
                    ConfigurationProperty configurationProperty = attached.findConfigurationProperty(name);
                    return (configurationProperty != null) ? configurationProperty.getValue() : null;
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return defaultResolver.getProperty(key, Object.class, false);
    }

    private ConfigurationPropertySourcesPropertySource getAttached() {
        ConfigurationPropertySourcesPropertySource attached = (ConfigurationPropertySourcesPropertySource) ConfigurationPropertySources.getAttached(propertySources);
        Iterable<ConfigurationPropertySource> iterable = ObjectUtils.defaultIfNullElseFunction(attached, PropertySource::getSource);
        return BooleanUtils.defaultIfFalse(iterable instanceof SpringConfigurationPropertySources && ((SpringConfigurationPropertySources) iterable).isUsingSources(propertySources), () -> attached);
    }

    static class DefaultResolver extends PropertySourcesPropertyResolver {

        DefaultResolver(PropertySources propertySources) {
            super(propertySources);
        }

        @Override
        public <T> T getProperty(String key, Class<T> targetValueType, boolean resolveNestedPlaceholders) {
            return super.getProperty(key, targetValueType, resolveNestedPlaceholders);
        }
    }
}