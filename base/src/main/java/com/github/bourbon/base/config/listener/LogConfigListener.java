package com.github.bourbon.base.config.listener;

import com.github.bourbon.base.config.ConfigKey;
import com.github.bourbon.base.config.ConfigSource;
import com.github.bourbon.base.config.ManagementListener;
import com.github.bourbon.base.lang.Prioritized;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 14:55
 */
public class LogConfigListener implements ManagementListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void afterConfigLoaded(ConfigKey<?> configKey, ConfigSource configSource, List<ConfigSource> configSourceList) {
        logger.info("Load {} from {} ,effect key is {}, value is \"{}\"", configKey.getKey(), configSource.getName(), configSource.getEffectiveKey(configKey), configSource.getStringConfig(configKey));
    }

    @Override
    public void onLoadDefaultValue(ConfigKey<?> key, Object defaultValue) {
        if (key.getDefaultValue().equals(defaultValue)) {
            logger.info("Load {} according defaultValue ,default value is \"{}\"", key.getKey(), defaultValue);
        } else {
            logger.warn("Config {}'s defaultValue {} does not equals to actually defaultValue {}", key.toString(), key.getDefaultValue(), defaultValue);
        }
    }

    @Override
    public int getPriority() {
        return Prioritized.MIN_PRIORITY;
    }
}