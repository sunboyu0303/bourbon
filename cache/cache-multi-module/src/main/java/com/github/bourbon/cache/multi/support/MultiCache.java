package com.github.bourbon.cache.multi.support;

import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.extension.support.ExtensionLoader;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.concurrent.AsyncBaseService;
import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheAdapter;
import com.github.bourbon.cache.core.CacheParamInfo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 16:57
 */
class MultiCache<K, V> implements Cache<K, V> {

    private static final ExtensionLoader<CacheAdapter> loader = ScopeModelUtils.getExtensionLoader(CacheAdapter.class);
    private final CacheParamInfo<K, V> info;
    private final Cache<K, V> ehcache;
    private final Cache<K, V> cache;

    MultiCache(CacheParamInfo<K, V> info) {
        this.info = info;
        this.ehcache = loader.getExtension("ehcache").getCache(info);
        this.cache = loader.getExtension("caffeine").getCache(
                new CacheParamInfo<K, V>().setMaximumSize(info.getMaximumSize()).setJvmDuration(info.getJvmDuration())
                        .setConcurrencyLevel(info.getConcurrencyLevel()).setInitialCapacity(info.getInitialCapacity())
                        .setTimeUnit(info.getTimeUnit()).setAsync(info.isAsync()).setThreadPoolSize(info.getThreadPoolSize())
                        .setApplyCache(info.getApplyCache()).setUniqueIdentifier(info.getUniqueIdentifier()).setCallBack(ehcache::get)
        );
    }

    @Override
    public V get(K k) {
        BooleanSupplier supplier = info.getApplyCache();
        if (!ObjectUtils.isNull(supplier) && supplier.getAsBoolean()) {
            return cache.get(k);
        }
        return info.getCallBack().apply(k);
    }

    @Override
    public V get(K k, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return AsyncBaseService.supplierAsync(() -> MultiCache.this.get(k), timeout, unit).get(timeout, unit);
    }

    @Override
    public void put(K k, V v) {
        BooleanSupplier supplier = info.getApplyCache();
        if (!ObjectUtils.isNull(supplier) && supplier.getAsBoolean()) {
            ehcache.put(k, v);
            cache.put(k, v);
        }
    }

    @Override
    public void refresh(K k) {
        BooleanSupplier supplier = info.getApplyCache();
        if (!ObjectUtils.isNull(supplier) && supplier.getAsBoolean()) {
            ehcache.refresh(k);
            cache.refresh(k);
        }
    }
}