package com.github.bourbon.cache.multi.support;

import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheAdapter;
import com.github.bourbon.cache.core.CacheParamInfo;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 16:58
 */
public class MultiCacheAdapter implements CacheAdapter {

    @Override
    public <K, V> Cache<K, V> getCache(CacheParamInfo<K, V> info) {
        return new MultiCache<>(info);
    }
}