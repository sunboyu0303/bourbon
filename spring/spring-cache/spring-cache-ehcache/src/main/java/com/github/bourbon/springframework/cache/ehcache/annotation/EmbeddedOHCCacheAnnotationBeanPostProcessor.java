package com.github.bourbon.springframework.cache.ehcache.annotation;

import com.github.bourbon.base.lang.mutable.MutableObject;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.cache.ehcache.annotation.EmbeddedOHCCache;
import com.github.bourbon.springframework.cache.core.annotation.AbstractCacheAnnotationBeanPostProcessor;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.beans.PropertyValues;
import org.springframework.util.ReflectionUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/7 18:31
 */
public class EmbeddedOHCCacheAnnotationBeanPostProcessor extends AbstractCacheAnnotationBeanPostProcessor {

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object object, String beanName) {
        Class<?> targetClass = object.getClass();
        do {
            ReflectionUtils.doWithLocalMethods(targetClass, method -> {
                EmbeddedOHCCache cache = AnnotationHelperUtils.getAnnotation(method, EmbeddedOHCCache.class);
                if (ObjectUtils.nonNull(cache) && method.getParameterCount() == 1) {
                    ReflectionUtils.makeAccessible(method);
                    beanFactory.getBean(EmbeddedOHCCacheAspect.class).getCache(method, cache, k -> {
                        try {
                            return new MutableObject<>(method.invoke(object, k.get()));
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    });
                }
            });
            targetClass = targetClass.getSuperclass();
        } while (ObjectUtils.nonNull(targetClass) && targetClass != Object.class);
        return pvs;
    }
}