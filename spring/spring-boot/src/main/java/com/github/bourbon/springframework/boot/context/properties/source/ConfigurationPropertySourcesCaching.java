package com.github.bourbon.springframework.boot.context.properties.source;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 22:32
 */
class ConfigurationPropertySourcesCaching implements ConfigurationPropertyCaching {

    private final Iterable<ConfigurationPropertySource> sources;

    ConfigurationPropertySourcesCaching(Iterable<ConfigurationPropertySource> sources) {
        this.sources = sources;
    }

    @Override
    public void enable() {
        forEach(ConfigurationPropertyCaching::enable);
    }

    @Override
    public void disable() {
        forEach(ConfigurationPropertyCaching::disable);
    }

    @Override
    public void setTimeToLive(Duration timeToLive) {
        forEach(c -> c.setTimeToLive(timeToLive));
    }

    @Override
    public void clear() {
        forEach(ConfigurationPropertyCaching::clear);
    }

    private void forEach(Consumer<ConfigurationPropertyCaching> action) {
        if (sources != null) {
            for (ConfigurationPropertySource source : sources) {
                ConfigurationPropertyCaching caching = CachingConfigurationPropertySource.find(source);
                if (caching != null) {
                    action.accept(caching);
                }
            }
        }
    }
}