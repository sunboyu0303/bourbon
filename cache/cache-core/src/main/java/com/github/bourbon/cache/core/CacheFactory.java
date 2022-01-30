package com.github.bourbon.cache.core;

import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.cache.core.support.NoCacheAdapter;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 17:04
 */
public final class CacheFactory {

    private static final Map<String, Cache<?, ?>> MAP = MapUtils.newConcurrentHashMap();

    private static final CacheAdapter cacheAdapter = BooleanUtils.defaultSupplierIfPredicate(
            ScopeModelUtils.getExtensionLoader(CacheAdapter.class).getSupportedExtensionInstances(), CollectionUtils::isNotEmpty, c -> c.iterator().next(), NoCacheAdapter::new
    );

    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> getCache(CacheParamInfo<K, V> info) {
        return (Cache<K, V>) MAP.computeIfAbsent(info.getUniqueIdentifier(), o -> cacheAdapter.getCache(info));
    }

    private CacheFactory() {
    }
}