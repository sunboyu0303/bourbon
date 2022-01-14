package com.github.bourbon.springframework.cache.multi.annotation;

import com.github.bourbon.base.lang.mutable.MutableObject;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheParamInfo;
import com.github.bourbon.cache.multi.annotation.JVMAndOHCCache;
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
 * @date 2022/1/7 23:25
 */
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class JVMAndOHCCacheAspect implements MultiCacheAspect {

    private final Map<String, Cache<MutableObject, MutableObject>> map = MapUtils.newConcurrentHashMap();

    @Around("@annotation(cache)")
    public Object around(ProceedingJoinPoint pjp, JVMAndOHCCache cache) throws Throwable {
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

    Cache<MutableObject, MutableObject> getCache(Method method, JVMAndOHCCache cache, Function<MutableObject, MutableObject> f) {
        return map.computeIfAbsent(CacheAspect.getKey(method),
                k -> cacheAdapter.getCache(new CacheParamInfo<MutableObject, MutableObject>()
                        .setMaximumSize(cache.maximumSize())
                        .setJvmDuration(cache.jvmDuration())
                        .setInitialCapacity(cache.initialCapacity())
                        .setTimeUnit(cache.timeUnit())
                        .setAsync(cache.async())
                        .setThreadPoolSize(cache.threadPoolSize())
                        .setUniqueIdentifier(k)
                        .setApplyCache(cache::applyCache)
                        .setCallBack(f)
                        .setKeyType(MutableObject.class)
                        .setMemoryUnit(cache.memoryUnit())
                        .setMemorySize(cache.memorySize())
                        .setOhcDuration(cache.ohcDuration())
                        .setTimeout(cache.timeout())
                        .setEmptyTimeout(cache.emptyTimeout())
                )
        );
    }
}