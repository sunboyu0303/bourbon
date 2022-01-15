package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationProperty;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertySource;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 15:10
 */
public interface BindContext {

    Binder getBinder();

    int getDepth();

    Iterable<ConfigurationPropertySource> getSources();

    ConfigurationProperty getConfigurationProperty();
}