package com.github.bourbon.springframework.cache.guava.annotation;

import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheParamInfo;
import com.github.bourbon.cache.guava.annotation.JVMCache;
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
 * @date 2022/1/7 17:37
 */
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class JVMCacheAspect implements GuavaCacheAspect {

    private Map<Method, Cache<Object, Object>> map = MapUtils.newConcurrentHashMap();

    @Around("@annotation(cache)")
    public Object around(ProceedingJoinPoint pjp, JVMCache cache) throws Throwable {
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

    Cache<Object, Object> getCache(Method method, JVMCache cache, Function<Object, Object> function) {
        return map.computeIfAbsent(method, k -> cacheAdapter.getCache(new CacheParamInfo<>()
                .setMaximumSize(cache.maximumSize())
                .setJvmDuration(cache.jvmDuration())
                .setConcurrencyLevel(cache.concurrencyLevel())
                .setInitialCapacity(cache.initialCapacity())
                .setTimeUnit(cache.timeUnit())
                .setThreadPoolSize(cache.threadPoolSize())
                .setUniqueIdentifier(CacheAspect.getKey(k))
                .setApplyCache(cache::applyCache)
                .setCallBack(function)
        ));
    }
}