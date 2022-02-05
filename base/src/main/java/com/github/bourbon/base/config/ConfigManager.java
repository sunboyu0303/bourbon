package com.github.bourbon.base.config;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 14:16
 */
public interface ConfigManager {

    <T> T getOrDefault(ConfigKey<T> key);

    <T> T getOrCustomDefault(ConfigKey<T> key, T customDefault);

    ConfigManager addConfigSource(ConfigSource configSource);

    ConfigManager addConfigListener(ManagementListener configListener);
}