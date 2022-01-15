package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.SystemPropertyUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 16:04
 */
public class PropertySourcesPlaceholdersResolver implements PlaceholdersResolver {

    private final Iterable<PropertySource<?>> sources;

    private final PropertyPlaceholderHelper helper;

    public PropertySourcesPlaceholdersResolver(Environment environment) {
        this(getSources(environment), null);
    }

    public PropertySourcesPlaceholdersResolver(Iterable<PropertySource<?>> sources) {
        this(sources, null);
    }

    public PropertySourcesPlaceholdersResolver(Iterable<PropertySource<?>> sources, PropertyPlaceholderHelper helper) {
        this.sources = sources;
        this.helper = ObjectUtils.defaultSupplierIfNull(helper, () -> new PropertyPlaceholderHelper(SystemPropertyUtils.PLACEHOLDER_PREFIX, SystemPropertyUtils.PLACEHOLDER_SUFFIX, SystemPropertyUtils.VALUE_SEPARATOR, true));
    }

    @Override
    public Object resolvePlaceholders(Object o) {
        return BooleanUtils.defaultIfAssignableFrom(o, String.class, v -> helper.replacePlaceholders((String) v, this::resolvePlaceholder), o);
    }

    protected String resolvePlaceholder(String placeholder) {
        if (sources != null) {
            for (PropertySource<?> source : sources) {
                Object value = source.getProperty(placeholder);
                if (value != null) {
                    return String.valueOf(value);
                }
            }
        }
        return null;
    }

    private static PropertySources getSources(Environment environment) {
        Assert.notNull(environment, "Environment must not be null");
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment, "Environment must be a ConfigurableEnvironment");
        return ((ConfigurableEnvironment) environment).getPropertySources();
    }
}