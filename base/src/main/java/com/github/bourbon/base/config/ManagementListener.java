package com.github.bourbon.base.config;

import com.github.bourbon.base.lang.Prioritized;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 14:19
 */
public interface ManagementListener extends Prioritized {

    default void beforeConfigLoading(ConfigKey<?> configKey, List<ConfigSource> configSources) {
    }

    default void afterConfigLoaded(ConfigKey<?> key, ConfigSource configSource, List<ConfigSource> configSourceList) {
    }

    default void onLoadDefaultValue(ConfigKey<?> key, Object defaultValue) {
    }
}