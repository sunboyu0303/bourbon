package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 15:53
 */
public interface IterableConfigurationPropertySource extends ConfigurationPropertySource, Iterable<ConfigurationPropertyName> {

    @Override
    default Iterator<ConfigurationPropertyName> iterator() {
        return stream().iterator();
    }

    Stream<ConfigurationPropertyName> stream();

    @Override
    default ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        return ConfigurationPropertyState.search(this, name::isAncestorOf);
    }

    @Override
    default IterableConfigurationPropertySource filter(Predicate<ConfigurationPropertyName> filter) {
        return new FilteredIterableConfigurationPropertiesSource(this, filter);
    }

    @Override
    default IterableConfigurationPropertySource withAliases(ConfigurationPropertyNameAliases aliases) {
        return new AliasedIterableConfigurationPropertySource(this, aliases);
    }

    @Override
    default IterableConfigurationPropertySource withPrefix(String prefix) {
        return BooleanUtils.defaultIfPredicate(prefix, p -> !CharSequenceUtils.isBlank(p), p -> new PrefixedIterableConfigurationPropertySource(this, p), this);
    }
}