package com.github.bourbon.springframework.cache.ehcache.annotation;

import com.github.bourbon.base.lang.mutable.MutableObject;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.TimeUnitUtils;
import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheParamInfo;
import com.github.bourbon.cache.core.utils.MemoryUnit;
import com.github.bourbon.cache.ehcache.annotation.EmbeddedOHCCache;
import com.github.bourbon.springframework.cache.core.annotation.AbstractCacheEmbeddedValueConverter;
import com.github.bourbon.springframework.cache.core.annotation.CacheAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/7 18:34
 */
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class EmbeddedOHCCacheAspect extends AbstractCacheEmbeddedValueConverter implements EhcacheCacheAspect {

    private Map<String, Cache<MutableObject, MutableObject>> map = MapUtils.newConcurrentHashMap();

    @Around("@annotation(cache)")
    public Object around(ProceedingJoinPoint pjp, EmbeddedOHCCache cache) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args.length != 1) {
            return pjp.proceed();
        }
        return getCache(((MethodSignature) pjp.getSignature()).getMethod(), cache, k -> {
            try {
                return new MutableObject<>(pjp.proceed());
            } catch (Throwable t) {
                throw new IllegalStateException(t);
            }
        }).get(new MutableObject<>(args[0])).get();
    }

    Cache<MutableObject, MutableObject> getCache(Method method, EmbeddedOHCCache cache, Function<MutableObject, MutableObject> f) {
        return map.computeIfAbsent(CacheAspect.getKey(method),
                k -> cacheAdapter.getCache(new CacheParamInfo<MutableObject, MutableObject>()
                        .setTimeUnit(TimeUnitUtils.getTimeUnit(getEmbeddedValue(cache.timeUnit())))
                        .setThreadPoolSize(getInteger(cache.threadPoolSize()))
                        .setUniqueIdentifier(k)
                        .setApplyCache(() -> getBoolean(cache.applyCache()))
                        .setCallBack(f)
                        .setKeyType(MutableObject.class)
                        .setMemoryUnit(MemoryUnit.getMemoryUnit(getEmbeddedValue(cache.memoryUnit())))
                        .setMemorySize(getLong(cache.memorySize()))
                        .setOhcDuration(getLong(cache.ohcDuration()))
                        .setTimeout(getLong(cache.timeout()))
                        .setEmptyTimeout(getLong(cache.emptyTimeout()))
                ));
    }
}