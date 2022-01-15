package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertySource;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 16:29
 */
@FunctionalInterface
interface AggregateElementBinder {

    default Object bind(ConfigurationPropertyName name, Bindable<?> target) {
        return bind(name, target, null);
    }

    Object bind(ConfigurationPropertyName name, Bindable<?> target, ConfigurationPropertySource source);
}