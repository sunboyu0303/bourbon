package com.github.bourbon.springframework.cache.caffeine.annotation;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.cache.caffeine.annotation.EmbeddedJVMCache;
import com.github.bourbon.springframework.cache.core.annotation.AbstractCacheAnnotationBeanPostProcessor;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.beans.PropertyValues;
import org.springframework.util.ReflectionUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/7 18:00
 */
public class EmbeddedJVMCacheAnnotationBeanPostProcessor extends AbstractCacheAnnotationBeanPostProcessor {

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object object, String beanName) {
        Class<?> targetClass = object.getClass();
        do {
            ReflectionUtils.doWithLocalMethods(targetClass, method -> {
                EmbeddedJVMCache cache = AnnotationHelperUtils.getAnnotation(method, EmbeddedJVMCache.class);
                if (ObjectUtils.nonNull(cache) && method.getParameterCount() == 1) {
                    ReflectionUtils.makeAccessible(method);
                    beanFactory.getBean(EmbeddedJVMCacheAspect.class).getCache(method, cache, k -> {
                        try {
                            return method.invoke(object, k);
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