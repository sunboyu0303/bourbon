package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.BooleanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 00:10
 */
class AliasedIterableConfigurationPropertySource extends AliasedConfigurationPropertySource implements IterableConfigurationPropertySource {

    AliasedIterableConfigurationPropertySource(IterableConfigurationPropertySource source, ConfigurationPropertyNameAliases aliases) {
        super(source, aliases);
    }

    @Override
    public Stream<ConfigurationPropertyName> stream() {
        return getSource().stream().flatMap(this::addAliases);
    }

    private Stream<ConfigurationPropertyName> addAliases(ConfigurationPropertyName name) {
        Stream<ConfigurationPropertyName> names = Stream.of(name);
        List<ConfigurationPropertyName> aliases = getAliases().getAliases(name);
        return BooleanUtils.defaultIfFalse(!CollectionUtils.isEmpty(aliases), () -> Stream.concat(names, aliases.stream()), names);
    }

    @Override
    protected IterableConfigurationPropertySource getSource() {
        return (IterableConfigurationPropertySource) super.getSource();
    }
}