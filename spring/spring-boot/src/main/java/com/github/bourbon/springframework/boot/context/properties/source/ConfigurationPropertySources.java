package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.env.*;

import java.util.stream.Stream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 16:02
 */
public final class ConfigurationPropertySources {

    private static final String ATTACHED_PROPERTY_SOURCE_NAME = "configurationProperties";

    private ConfigurationPropertySources() {
    }

    public static ConfigurablePropertyResolver createPropertyResolver(MutablePropertySources propertySources) {
        return new ConfigurationPropertySourcesPropertyResolver(propertySources);
    }

    public static boolean isAttachedConfigurationPropertySource(PropertySource<?> propertySource) {
        return ATTACHED_PROPERTY_SOURCE_NAME.equals(propertySource.getName());
    }

    public static void attach(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
        PropertySource<?> attached = getAttached(sources);
        if (attached != null && attached.getSource() != sources) {
            sources.remove(ATTACHED_PROPERTY_SOURCE_NAME);
            attached = null;
        }
        if (attached == null) {
            sources.addFirst(new ConfigurationPropertySourcesPropertySource(ATTACHED_PROPERTY_SOURCE_NAME, new SpringConfigurationPropertySources(sources)));
        }
    }

    static PropertySource<?> getAttached(MutablePropertySources sources) {
        return ObjectUtils.defaultIfNull(sources, s -> s.get(ATTACHED_PROPERTY_SOURCE_NAME));
    }

    public static Iterable<ConfigurationPropertySource> get(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
        return ObjectUtils.defaultSupplierIfNull((ConfigurationPropertySourcesPropertySource) sources.get(ATTACHED_PROPERTY_SOURCE_NAME), PropertySource::getSource, () -> from(sources));
    }

    public static Iterable<ConfigurationPropertySource> from(PropertySource<?> source) {
        return SetUtils.newHashSet(ConfigurationPropertySource.from(source));
    }

    public static Iterable<ConfigurationPropertySource> from(Iterable<PropertySource<?>> sources) {
        return new SpringConfigurationPropertySources(sources);
    }

    private static Stream<PropertySource<?>> streamPropertySources(PropertySources sources) {
        return sources.stream().flatMap(ConfigurationPropertySources::flatten).filter(ConfigurationPropertySources::isIncluded);
    }

    private static Stream<PropertySource<?>> flatten(PropertySource<?> source) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(source.getSource(), ConfigurableEnvironment.class, s -> streamPropertySources(((ConfigurableEnvironment) s).getPropertySources()), () -> Stream.of(source));
    }

    private static boolean isIncluded(PropertySource<?> source) {
        return !(source instanceof PropertySource.StubPropertySource) && !(source instanceof ConfigurationPropertySourcesPropertySource);
    }
}