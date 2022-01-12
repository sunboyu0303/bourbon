package com.github.bourbon.cache.ehcache.support;

import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheAdapter;
import com.github.bourbon.cache.core.CacheParamInfo;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 22:50
 */
public class EhcacheAdapter implements CacheAdapter {

    @Override
    public <K, V> Cache<K, V> getCache(CacheParamInfo<K, V> info) {
        return new Ehcache<>(info);
    }
}