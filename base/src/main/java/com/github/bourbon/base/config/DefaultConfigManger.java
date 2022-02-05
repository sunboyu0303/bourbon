package com.github.bourbon.base.config;

import com.github.bourbon.base.lang.Prioritized;
import com.github.bourbon.base.utils.ListUtils;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 14:19
 */
public class DefaultConfigManger implements ConfigManager {

    private final List<ConfigSource> configSources = ListUtils.newLinkedList();
    private final List<ManagementListener> configListeners = ListUtils.newLinkedList();

    @Override
    public <T> T getOrDefault(ConfigKey<T> key) {
        return getConfig(key, key.getDefaultValue());
    }

    @Override
    public <T> T getOrCustomDefault(ConfigKey<T> key, T customDefault) {
        return getConfig(key, customDefault);
    }

    private <T> T getConfig(ConfigKey<T> key, T defaultValue) {
        T result;
        beforeConfigLoading(key);
        for (ConfigSource configSource : configSources) {
            result = configSource.getConfig(key);
            if (result != null) {
                afterConfigLoaded(key, configSource);
                return result;
            }
        }
        onLoadDefaultValue(key, defaultValue);
        return defaultValue;
    }

    private <T> void beforeConfigLoading(ConfigKey<T> key) {
        configListeners.forEach(l -> l.beforeConfigLoading(key, configSources));
    }

    private <T> void afterConfigLoaded(ConfigKey<T> key, ConfigSource configSource) {
        configListeners.forEach(l -> l.afterConfigLoaded(key, configSource, configSources));
    }

    private <T> void onLoadDefaultValue(ConfigKey<T> key, Object defaultValue) {
        configListeners.forEach(l -> l.onLoadDefaultValue(key, defaultValue));
    }

    @Override
    public ConfigManager addConfigSource(ConfigSource configSource) {
        configSources.add(configSource);
        configSources.sort(Prioritized.COMPARATOR);
        return this;
    }

    @Override
    public ConfigManager addConfigListener(ManagementListener configListener) {
        configListeners.add(configListener);
        configListeners.sort(Prioritized.COMPARATOR);
        return this;
    }
}