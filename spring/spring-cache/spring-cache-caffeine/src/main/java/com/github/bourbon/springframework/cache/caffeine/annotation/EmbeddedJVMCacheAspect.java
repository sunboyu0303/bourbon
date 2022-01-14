package com.github.bourbon.springframework.cache.caffeine.annotation;

import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.TimeUnitUtils;
import com.github.bourbon.cache.caffeine.annotation.EmbeddedJVMCache;
import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheParamInfo;
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
 * @date 2022/1/7 18:02
 */
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class EmbeddedJVMCacheAspect extends AbstractCacheEmbeddedValueConverter implements CaffeineCacheAspect {

    private final Map<String, Cache<Object, Object>> map = MapUtils.newConcurrentHashMap();

    @Around("@annotation(cache)")
    public Object around(ProceedingJoinPoint pjp, EmbeddedJVMCache cache) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args.length != 1) {
            return pjp.proceed();
        }
        return getCache(((MethodSignature) pjp.getSignature()).getMethod(), cache, k -> {
            try {
                return pjp.proceed();
            } catch (Throwable t) {
                throw new IllegalStateException(t);
            }
        }).get(args[0]);
    }

    Cache<Object, Object> getCache(Method method, EmbeddedJVMCache cache, Function<Object, Object> f) {
        return map.computeIfAbsent(CacheAspect.getKey(method),
                k -> cacheAdapter.getCache(new CacheParamInfo<>()
                        .setMaximumSize(getLong(cache.maximumSize()))
                        .setJvmDuration(getLong(cache.jvmDuration()))
                        .setInitialCapacity(getInteger(cache.initialCapacity()))
                        .setTimeUnit(TimeUnitUtils.getTimeUnit(getEmbeddedValue(cache.timeUnit())))
                        .setAsync(getBoolean(cache.async()))
                        .setThreadPoolSize(getInteger(cache.threadPoolSize()))
                        .setUniqueIdentifier(k)
                        .setApplyCache(() -> getBoolean(cache.applyCache()))
                        .setCallBack(f)
                )
        );
    }
}