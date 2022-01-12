package com.github.bourbon.cache.core;

import com.github.bourbon.cache.core.utils.MemoryUnit;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 17:06
 */
public class CacheParamInfo<K, V> {

    private long maximumSize;
    private long jvmDuration;
    private int concurrencyLevel;
    private int initialCapacity;
    private TimeUnit timeUnit;
    private boolean async;
    private int threadPoolSize;
    private BooleanSupplier applyCache;
    private String uniqueIdentifier;
    private Function<K, V> callBack;

    private Class<K> keyType;
    private MemoryUnit memoryUnit;
    private long memorySize;
    private long ohcDuration;
    private long timeout;
    private long emptyTimeout;

    public long getMaximumSize() {
        return maximumSize;
    }

    public CacheParamInfo<K, V> setMaximumSize(long maximumSize) {
        this.maximumSize = maximumSize;
        return this;
    }

    public long getJvmDuration() {
        return jvmDuration;
    }

    public CacheParamInfo<K, V> setJvmDuration(long jvmDuration) {
        this.jvmDuration = jvmDuration;
        return this;
    }

    public int getConcurrencyLevel() {
        return concurrencyLevel;
    }

    public CacheParamInfo<K, V> setConcurrencyLevel(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public CacheParamInfo<K, V> setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
        return this;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public CacheParamInfo<K, V> setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public boolean isAsync() {
        return async;
    }

    public CacheParamInfo<K, V> setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public CacheParamInfo<K, V> setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        return this;
    }

    public BooleanSupplier getApplyCache() {
        return applyCache;
    }

    public CacheParamInfo<K, V> setApplyCache(BooleanSupplier applyCache) {
        this.applyCache = applyCache;
        return this;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public CacheParamInfo<K, V> setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
        return this;
    }

    public Function<K, V> getCallBack() {
        return callBack;
    }

    public CacheParamInfo<K, V> setCallBack(Function<K, V> callBack) {
        this.callBack = callBack;
        return this;
    }

    public Class<K> getKeyType() {
        return keyType;
    }

    public CacheParamInfo<K, V> setKeyType(Class<K> keyType) {
        this.keyType = keyType;
        return this;
    }

    public MemoryUnit getMemoryUnit() {
        return memoryUnit;
    }

    public CacheParamInfo<K, V> setMemoryUnit(MemoryUnit memoryUnit) {
        this.memoryUnit = memoryUnit;
        return this;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public CacheParamInfo<K, V> setMemorySize(long memorySize) {
        this.memorySize = memorySize;
        return this;
    }

    public long getOhcDuration() {
        return ohcDuration;
    }

    public CacheParamInfo<K, V> setOhcDuration(long ohcDuration) {
        this.ohcDuration = ohcDuration;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public CacheParamInfo<K, V> setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public long getEmptyTimeout() {
        return emptyTimeout;
    }

    public CacheParamInfo<K, V> setEmptyTimeout(long emptyTimeout) {
        this.emptyTimeout = emptyTimeout;
        return this;
    }
}