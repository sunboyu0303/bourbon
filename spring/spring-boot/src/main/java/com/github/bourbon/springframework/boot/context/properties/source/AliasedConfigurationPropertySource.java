package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 15:44
 */
class AliasedConfigurationPropertySource implements ConfigurationPropertySource {

    private final ConfigurationPropertySource source;
    private final ConfigurationPropertyNameAliases aliases;

    AliasedConfigurationPropertySource(ConfigurationPropertySource source, ConfigurationPropertyNameAliases aliases) {
        this.source = ObjectUtils.requireNonNull(source, "Source must not be null");
        this.aliases = ObjectUtils.requireNonNull(aliases, "Aliases must not be null");
    }

    @Override
    public ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name) {
        Assert.notNull(name, "Name must not be null");
        ConfigurationProperty result = getSource().getConfigurationProperty(name);
        if (result == null) {
            result = getSource().getConfigurationProperty(getAliases().getNameForAlias(name));
        }
        return result;
    }

    @Override
    public ConfigurationPropertyState containsDescendantOf(ConfigurationPropertyName name) {
        Assert.notNull(name, "Name must not be null");
        ConfigurationPropertyState result = source.containsDescendantOf(name);
        if (result != ConfigurationPropertyState.ABSENT) {
            return result;
        }
        for (ConfigurationPropertyName alias : getAliases().getAliases(name)) {
            ConfigurationPropertyState aliasResult = source.containsDescendantOf(alias);
            if (aliasResult != ConfigurationPropertyState.ABSENT) {
                return aliasResult;
            }
        }
        for (ConfigurationPropertyName from : getAliases()) {
            for (ConfigurationPropertyName alias : getAliases().getAliases(from)) {
                if (name.isAncestorOf(alias)) {
                    if (source.getConfigurationProperty(from) != null) {
                        return ConfigurationPropertyState.PRESENT;
                    }
                }
            }
        }
        return ConfigurationPropertyState.ABSENT;
    }

    @Override
    public Object getUnderlyingSource() {
        return source.getUnderlyingSource();
    }

    protected ConfigurationPropertySource getSource() {
        return source;
    }

    protected ConfigurationPropertyNameAliases getAliases() {
        return aliases;
    }
}