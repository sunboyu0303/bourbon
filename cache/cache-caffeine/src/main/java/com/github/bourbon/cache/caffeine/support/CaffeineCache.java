package com.github.bourbon.cache.caffeine.support;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.IntUtils;
import com.github.bourbon.base.utils.LongUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.concurrent.AsyncBaseService;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.utils.concurrent.ThreadPoolExecutorFactory;
import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheParamInfo;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/14 14:39
 */
class CaffeineCache<K, V> implements Cache<K, V> {

    private final CacheParamInfo<K, V> info;
    private final LoadingCache<K, Optional<V>> cache;
    private final AsyncLoadingCache<K, Optional<V>> asyncCache;

    CaffeineCache(CacheParamInfo<K, V> info) {
        this.info = ObjectUtils.requireNonNull(info, "info == null");

        Assert.notNull(info.getUniqueIdentifier(), "info.uniqueIdentifier == null");
        Assert.notNull(info.getCallBack(), "info.callBack == null");
        Assert.notNull(info.getTimeUnit(), "info.timeUnit == null");
        LongUtils.checkPositive(info.getJvmDuration(), "info.jvmDuration");

        long time = info.getTimeUnit().toMillis(info.getJvmDuration());
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder().maximumSize(info.getMaximumSize())
                .initialCapacity(info.getInitialCapacity()).refreshAfterWrite(time, TimeUnit.MILLISECONDS)
                .expireAfterWrite(time + time / 5, TimeUnit.MILLISECONDS).recordStats();

        if (info.isAsync()) {
            caffeine.executor(ThreadPoolExecutorFactory.newFixedThreadPool("caffeine", IntUtils.checkPositive(info.getThreadPoolSize(), "info.threadPoolSize"), new NamedThreadFactory(getClass().getName() + StringConstants.HYPHEN + info.getUniqueIdentifier())));
            asyncCache = caffeine.buildAsync((K k, Executor e) -> CompletableFuture.supplyAsync(() -> Optional.ofNullable(info.getCallBack().apply(k)), e));
            cache = asyncCache.synchronous();
        } else {
            asyncCache = null;
            cache = caffeine.build(k -> Optional.ofNullable(info.getCallBack().apply(k)));
        }
    }

    @Override
    public V get(K k) {
        BooleanSupplier supplier = info.getApplyCache();
        if (!ObjectUtils.isNull(supplier) && supplier.getAsBoolean()) {
            return cache.get(k).orElse(null);
        }
        return info.getCallBack().apply(k);
    }

    @Override
    public CompletableFuture<V> getAsync(K k) {
        if (info.isAsync()) {
            return AsyncBaseService.supplierAsync(() -> asyncCache.get(k).join().orElse(null));
        }
        return AsyncBaseService.supplierAsync(() -> CaffeineCache.this.get(k));
    }

    @Override
    public void put(K k, V v) {
        BooleanSupplier supplier = info.getApplyCache();
        if (!ObjectUtils.isNull(supplier) && supplier.getAsBoolean()) {
            cache.put(k, Optional.ofNullable(v));
        }
    }

    @Override
    public void refresh(K k) {
        BooleanSupplier supplier = info.getApplyCache();
        if (!ObjectUtils.isNull(supplier) && supplier.getAsBoolean()) {
            cache.refresh(k);
        }
    }
}