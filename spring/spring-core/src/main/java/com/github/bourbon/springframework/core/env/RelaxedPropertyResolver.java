package com.github.bourbon.springframework.core.env;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.core.env.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/30 17:29
 */
public class RelaxedPropertyResolver implements PropertyResolver {

    private final PropertyResolver resolver;

    private final String prefix;

    public RelaxedPropertyResolver(PropertyResolver resolver, String prefix) {
        Assert.notNull(resolver, "PropertyResolver must not be null");
        this.resolver = resolver;
        this.prefix = ObjectUtils.defaultIfNull(prefix, StringConstants.EMPTY);
    }

    @Override
    public boolean containsProperty(String key) {
        return getProperty(key) != null;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, String.class);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return ObjectUtils.defaultIfNull(getProperty(key), defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        for (String tmp : new RelaxedNames(prefix)) {
            for (String k : new RelaxedNames(key)) {
                String concat = tmp + k;
                if (resolver.containsProperty(concat)) {
                    return resolver.getProperty(concat, targetType);
                }
            }
        }
        return null;
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        for (String tmp : new RelaxedNames(prefix)) {
            for (String k : new RelaxedNames(key)) {
                String concat = tmp + k;
                if (resolver.containsProperty(concat)) {
                    return ObjectUtils.defaultIfNull(resolver.getProperty(concat, targetType), defaultValue);
                }
            }
        }
        return defaultValue;
    }

    @Override
    public String getRequiredProperty(String key) {
        return getRequiredProperty(key, String.class);
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) {
        return ObjectUtils.requireNonNull(getProperty(key, targetType), () -> "Required key '" + key + "' not found");
    }

    @Override
    public String resolvePlaceholders(String text) {
        return CharSequenceUtils.requireNotEmpty(resolver.resolvePlaceholders(text), () -> "Placeholders text '" + text + "' not found");
    }

    @Override
    public String resolveRequiredPlaceholders(String text) {
        return resolver.resolveRequiredPlaceholders(text);
    }

    public Map<String, Object> getSubProperties(String keyPrefix) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, resolver, "SubProperties not available.");
        return getSubProperties(((ConfigurableEnvironment) resolver).getPropertySources(), prefix, keyPrefix);
    }

    private Map<String, Object> getSubProperties(PropertySources propertySources, String rootPrefix, String keyPrefix) {
        RelaxedNames keyPrefixes = new RelaxedNames(keyPrefix);
        Map<String, Object> subProperties = new LinkedHashMap<>();
        final Iterator<PropertySource<?>> iterator = propertySources.iterator();

        while (true) {
            PropertySource<?> source;
            do {
                if (!iterator.hasNext()) {
                    return Collections.unmodifiableMap(subProperties);
                }
                source = iterator.next();
            } while (!(source instanceof EnumerablePropertySource));

            for (String name : ((EnumerablePropertySource) source).getPropertyNames()) {
                String key = getSubKey(name, rootPrefix, keyPrefixes);
                if (ObjectUtils.nonNull(key)) {
                    PropertySource<?> tmp = source;
                    subProperties.computeIfAbsent(key, o -> tmp.getProperty(name));
                }
            }
        }
    }

    private static String getSubKey(String name, String rootPrefixes, RelaxedNames keyPrefix) {
        for (String rootPrefix : new RelaxedNames(ObjectUtils.defaultIfNull(rootPrefixes, StringConstants.EMPTY))) {
            for (String candidateKeyPrefix : keyPrefix) {
                if (name.startsWith(rootPrefix + candidateKeyPrefix)) {
                    return name.substring((rootPrefix + candidateKeyPrefix).length());
                }
            }
        }
        return null;
    }
}