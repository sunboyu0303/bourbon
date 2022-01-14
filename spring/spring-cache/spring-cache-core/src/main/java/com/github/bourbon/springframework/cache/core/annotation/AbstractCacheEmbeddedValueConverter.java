package com.github.bourbon.springframework.cache.core.annotation;

import com.github.bourbon.springframework.context.annotation.CacheEmbeddedValueConverter;
import org.springframework.util.StringValueResolver;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/7 18:07
 */
public abstract class AbstractCacheEmbeddedValueConverter implements CacheEmbeddedValueConverter {

    private StringValueResolver embeddedValueResolver;

    @Override
    public StringValueResolver getEmbeddedValueResolver() {
        return embeddedValueResolver;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver embeddedValueResolver) {
        this.embeddedValueResolver = embeddedValueResolver;
    }
}