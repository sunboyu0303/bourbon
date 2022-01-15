package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.ListUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 15:39
 */
public final class ConfigurationPropertyNameAliases implements Iterable<ConfigurationPropertyName> {

    private final MultiValueMap<ConfigurationPropertyName, ConfigurationPropertyName> aliases = new LinkedMultiValueMap<>();

    public ConfigurationPropertyNameAliases() {
    }

    public ConfigurationPropertyNameAliases(String name, String... aliases) {
        addAliases(name, aliases);
    }

    public ConfigurationPropertyNameAliases(ConfigurationPropertyName name, ConfigurationPropertyName... aliases) {
        addAliases(name, aliases);
    }

    public void addAliases(String name, String... aliases) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(aliases, "Aliases must not be null");
        this.aliases.addAll(ConfigurationPropertyName.of(name),
                Arrays.stream(aliases).map(ConfigurationPropertyName::of).collect(Collectors.toList()));
    }

    public void addAliases(ConfigurationPropertyName name, ConfigurationPropertyName... aliases) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(aliases, "Aliases must not be null");
        this.aliases.addAll(name, ListUtils.newArrayList(aliases));
    }

    public List<ConfigurationPropertyName> getAliases(ConfigurationPropertyName name) {
        return aliases.getOrDefault(name, Collections.emptyList());
    }

    public ConfigurationPropertyName getNameForAlias(ConfigurationPropertyName alias) {
        return aliases.entrySet().stream().filter(e -> e.getValue().contains(alias)).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    @Override
    public Iterator<ConfigurationPropertyName> iterator() {
        return aliases.keySet().iterator();
    }
}