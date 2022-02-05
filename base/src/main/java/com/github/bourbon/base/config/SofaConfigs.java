package com.github.bourbon.base.config;

import com.github.bourbon.base.config.listener.LogConfigListener;
import com.github.bourbon.base.config.source.SystemEnvConfigSource;
import com.github.bourbon.base.config.source.SystemPropertyConfigSource;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 14:32
 */
public final class SofaConfigs {

    private static final ConfigManager INSTANCE = new DefaultConfigManger()
            .addConfigSource(new SystemPropertyConfigSource())
            .addConfigSource(new SystemEnvConfigSource())
            .addConfigListener(new LogConfigListener());

    public static <T> T getOrDefault(ConfigKey<T> key) {
        return INSTANCE.getOrDefault(key);
    }

    public static <T> T getOrCustomDefault(ConfigKey<T> key, T customDefault) {
        return INSTANCE.getOrCustomDefault(key, customDefault);
    }

    public static void addConfigSource(ConfigSource configSource) {
        INSTANCE.addConfigSource(configSource);
    }

    public static void addConfigListener(ManagementListener listener) {
        INSTANCE.addConfigListener(listener);
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    private SofaConfigs() {
    }
}