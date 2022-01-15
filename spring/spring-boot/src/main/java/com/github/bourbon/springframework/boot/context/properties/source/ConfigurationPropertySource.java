package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import org.springframework.core.env.PropertySource;

import java.util.function.Predicate;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 16:01
 */
@FunctionalInterface
public interface ConfigurationPropertySource {

    ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name);

    default ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        return ConfigurationPropertyState.UNKNOWN;
    }

    default ConfigurationPropertySource filter(Predicate<ConfigurationPropertyName> filter) {
        return new FilteredConfigurationPropertiesSource(this, filter);
    }

    default ConfigurationPropertySource withAliases(ConfigurationPropertyNameAliases aliases) {
        return new AliasedConfigurationPropertySource(this, aliases);
    }

    default ConfigurationPropertySource withPrefix(String prefix) {
        return BooleanUtils.defaultIfPredicate(prefix, p -> !CharSequenceUtils.isEmpty(p), p -> new PrefixedConfigurationPropertySource(this, p), this);
    }

    default Object getUnderlyingSource() {
        return null;
    }

    static ConfigurationPropertySource from(PropertySource<?> source) {
        return BooleanUtils.defaultIfFalse(!(source instanceof ConfigurationPropertySourcesPropertySource), () -> SpringConfigurationPropertySource.from(source));
    }
}