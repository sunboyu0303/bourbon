package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.lang.Assert;
import org.springframework.core.env.Environment;

import java.time.Duration;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 22:29
 */
public interface ConfigurationPropertyCaching {

    void enable();

    void disable();

    void setTimeToLive(Duration timeToLive);

    void clear();

    static ConfigurationPropertyCaching get(Environment environment) {
        return get(environment, null);
    }

    static ConfigurationPropertyCaching get(Environment environment, Object underlyingSource) {
        return get(ConfigurationPropertySources.get(environment), underlyingSource);
    }

    static ConfigurationPropertyCaching get(Iterable<ConfigurationPropertySource> sources) {
        return get(sources, null);
    }

    static ConfigurationPropertyCaching get(Iterable<ConfigurationPropertySource> sources, Object underlyingSource) {
        Assert.notNull(sources, "Sources must not be null");
        if (underlyingSource == null) {
            return new ConfigurationPropertySourcesCaching(sources);
        }
        for (ConfigurationPropertySource source : sources) {
            if (source.getUnderlyingSource() == underlyingSource) {
                ConfigurationPropertyCaching caching = CachingConfigurationPropertySource.find(source);
                if (caching != null) {
                    return caching;
                }
            }
        }
        throw new IllegalStateException("Unable to find cache from configuration property sources");
    }
}