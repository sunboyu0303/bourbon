package com.github.bourbon.cache.caffeine.support;

import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheAdapter;
import com.github.bourbon.cache.core.CacheParamInfo;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/14 14:40
 */
public class CaffeineCacheAdapter implements CacheAdapter {

    @Override
    public <K, V> Cache<K, V> getCache(CacheParamInfo<K, V> info) {
        return new CaffeineCache<>(info);
    }
}