package com.github.bourbon.base.config;

import com.github.bourbon.base.lang.Prioritized;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 14:17
 */
public interface ConfigSource extends Prioritized {

    <T> T getConfig(ConfigKey<T> key);

    String getName();

    String getStringConfig(ConfigKey<?> key);

    String getEffectiveKey(ConfigKey<?> configKey);
}