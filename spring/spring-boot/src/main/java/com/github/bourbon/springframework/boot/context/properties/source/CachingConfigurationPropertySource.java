package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.BooleanUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 22:23
 */
interface CachingConfigurationPropertySource {

    ConfigurationPropertyCaching getCaching();

    static ConfigurationPropertyCaching find(ConfigurationPropertySource source) {
        return BooleanUtils.defaultIfAssignableFrom(source, CachingConfigurationPropertySource.class, s -> ((CachingConfigurationPropertySource) s).getCaching());
    }
}