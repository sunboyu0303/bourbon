package com.github.bourbon.cache.ehcache.support;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.lang.statistic.WindowWrap;
import com.github.bourbon.base.utils.IntUtils;
import com.github.bourbon.base.utils.LongUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.concurrent.AsyncBaseService;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.utils.concurrent.ThreadPoolExecutorFactory;
import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheParamInfo;
import com.github.bourbon.cache.ehcache.utils.MemoryUnitUtils;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.internal.statistics.DefaultStatisticsService;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.BooleanSupplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 22:49
 */
class Ehcache<K, V> implements Cache<K, V> {

    private static final CacheManager CACHE_MANAGER = CacheManagerBuilder.newCacheManagerBuilder().using(new DefaultStatisticsService()).build(true);
    private static final int TOTAL = 10000;
    private static final int HALF_TOTAL = 5000;

    private final CacheParamInfo<K, V> info;
    private final org.ehcache.Cache<K, WindowWrap> cache;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final long timeoutMillis;
    private final long emptyTimeoutMillis;

    Ehcache(CacheParamInfo<K, V> info) {
        this.info = ObjectUtils.requireNonNull(info, "info == null");

        Assert.notNull(info.getUniqueIdentifier(), "info.uniqueIdentifier == null");
        Assert.notNull(info.getCallBack(), "info.callBack == null");
        Assert.notNull(info.getTimeUnit(), "info.timeUnit == null");
        Assert.notNull(info.getKeyType(), "info.keyType == null");
        LongUtils.checkPositive(info.getOhcDuration(), "info.ohcDuration");

        long expireTime = info.getTimeUnit().toMillis(info.getOhcDuration());
        timeoutMillis = info.getTimeUnit().toMillis(info.getTimeout());
        emptyTimeoutMillis = info.getTimeUnit().toMillis(info.getEmptyTimeout());

        threadPoolExecutor = ThreadPoolExecutorFactory.newFixedThreadPool("ehcache", IntUtils.checkPositive(info.getThreadPoolSize(), "info.threadPoolSize"),
                new NamedThreadFactory(getClass().getName() + StringConstants.HYPHEN + info.getUniqueIdentifier()));

        cache = CACHE_MANAGER.createCache(info.getUniqueIdentifier(),
                CacheConfigurationBuilder.newCacheConfigurationBuilder(info.getKeyType(), WindowWrap.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(info.getMemorySize(), MemoryUnitUtils.getMemoryUnit(info.getMemoryUnit().name())))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMillis(expireTime)))
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(K k) {
        BooleanSupplier supplier = info.getApplyCache();
        if (!ObjectUtils.isNull(supplier) && supplier.getAsBoolean()) {
            WindowWrap<V> wrap = (WindowWrap<V>) cache.get(k);
            if (ObjectUtils.isNull(wrap)) {
                V v = info.getCallBack().apply(k);
                threadPoolExecutor.execute(() -> put(k, v));
                return v;
            } else if (Clock.currentTimeMillis() >= wrap.windowLength()) {
                threadPoolExecutor.execute(() -> refresh(k));
            }
            return wrap.get();
        }
        return info.getCallBack().apply(k);
    }

    @Override
    public V get(K k, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return AsyncBaseService.supplierAsync(() -> Ehcache.this.get(k), timeout, unit).get(timeout, unit);
    }

    @Override
    public void put(K k, V v) {
        BooleanSupplier supplier = info.getApplyCache();
        if (!ObjectUtils.isNull(supplier) && supplier.getAsBoolean()) {
            long time;
            if (ObjectUtils.isNull(v) && emptyTimeoutMillis > 0) {
                time = Clock.currentTimeMillis() + emptyTimeoutMillis;
            } else {
                time = Clock.currentTimeMillis() + (timeoutMillis * (HALF_TOTAL + ThreadLocalRandom.current().nextInt(HALF_TOTAL)) / TOTAL);
            }
            cache.put(k, new WindowWrap<>(time, v));
        }
    }

    @Override
    public void refresh(K k) {
        BooleanSupplier supplier = info.getApplyCache();
        if (!ObjectUtils.isNull(supplier) && supplier.getAsBoolean()) {
            put(k, info.getCallBack().apply(k));
        }
    }
}