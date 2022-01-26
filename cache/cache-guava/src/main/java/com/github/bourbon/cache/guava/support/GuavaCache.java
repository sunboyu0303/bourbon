package com.github.bourbon.cache.guava.support;

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
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 21:56
 */
class GuavaCache<K, V> implements Cache<K, V> {

    private final CacheParamInfo<K, V> info;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final LoadingCache<K, Optional<V>> cache;

    GuavaCache(CacheParamInfo<K, V> info) {
        this.info = ObjectUtils.requireNonNull(info, "info == null");

        Assert.notNull(info.getUniqueIdentifier(), "info.uniqueIdentifier == null");
        Assert.notNull(info.getCallBack(), "info.callBack == null");
        Assert.notNull(info.getTimeUnit(), "info.timeUnit == null");
        LongUtils.checkPositive(info.getJvmDuration(), "info.jvmDuration");

        threadPoolExecutor = ThreadPoolExecutorFactory.newFixedThreadPool("guava", IntUtils.checkPositive(info.getThreadPoolSize(), "info.threadPoolSize"),
                new NamedThreadFactory(getClass().getName() + StringConstants.HYPHEN + info.getUniqueIdentifier()));

        long time = info.getTimeUnit().toMillis(info.getJvmDuration());

        cache = CacheBuilder.newBuilder()
                .maximumSize(info.getMaximumSize())
                .initialCapacity(info.getInitialCapacity())
                .concurrencyLevel(info.getConcurrencyLevel())
                .refreshAfterWrite(time, TimeUnit.MILLISECONDS)
                .expireAfterWrite(time + time / 5, TimeUnit.MILLISECONDS)
                .recordStats()
                .build(new CacheLoader<K, Optional<V>>() {

                    @Override
                    public Optional<V> load(K k) {
                        return Optional.ofNullable(info.getCallBack().apply(k));
                    }

                    @Override
                    public ListenableFuture<Optional<V>> reload(K key, Optional<V> oldValue) throws Exception {
                        try {
                            ListenableFutureTask<Optional<V>> task = ListenableFutureTask.create(() -> load(key));
                            threadPoolExecutor.submit(task);
                            return task;
                        } catch (Exception e) {
                            return super.reload(key, oldValue);
                        }
                    }
                });
    }

    @Override
    public V get(K k) {
        BooleanSupplier supplier = info.getApplyCache();
        if (!ObjectUtils.isNull(supplier) && supplier.getAsBoolean()) {
            return cache.getUnchecked(k).orElse(null);
        }
        return info.getCallBack().apply(k);
    }

    @Override
    public CompletableFuture<V> getAsync(K k) {
        return AsyncBaseService.supplierAsync(() -> GuavaCache.this.get(k));
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