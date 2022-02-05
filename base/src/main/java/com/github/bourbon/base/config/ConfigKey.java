package com.github.bourbon.base.config;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 14:07
 */
public final class ConfigKey<T> {

    private final String key;
    private final String[] alias;
    private final T defaultValue;
    private final String description;
    private final boolean modifiable;

    private ConfigKey(String key, String[] alias, T defaultValue, String description, boolean modifiable) {
        this.key = ObjectUtils.requireNonNull(key, "\"key\" of ConfigKey cannot be null, please check it.");
        this.alias = ObjectUtils.defaultIfNull(alias, StringConstants.EMPTY_STRING_ARRAY);
        this.defaultValue = ObjectUtils.requireNonNull(defaultValue, "\"defaultValue\" of ConfigKey cannot be null, please check it.");
        this.description = ObjectUtils.requireNonNull(description, "\"description\" of ConfigKey cannot be null, please check it.");
        this.modifiable = modifiable;
    }

    public String getKey() {
        return key;
    }

    public String[] getAlias() {
        return Arrays.copyOf(alias, alias.length);
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public static <T> ConfigKey<T> build(String key, T defaultValue, String description, boolean modifiable) {
        return build(key, null, defaultValue, description, modifiable);
    }

    public static <T> ConfigKey<T> build(String key, String[] alias, T defaultValue, String description, boolean modifiable) {
        return new ConfigKey<>(key, alias, defaultValue, description, modifiable);
    }
}