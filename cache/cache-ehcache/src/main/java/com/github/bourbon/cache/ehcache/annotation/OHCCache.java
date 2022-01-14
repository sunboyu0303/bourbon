package com.github.bourbon.cache.ehcache.annotation;

import com.github.bourbon.cache.core.utils.MemoryUnit;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 17:01
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OHCCache {

    TimeUnit timeUnit() default TimeUnit.MINUTES;

    int threadPoolSize() default 8;

    boolean applyCache() default true;

    MemoryUnit memoryUnit() default MemoryUnit.MB;

    long memorySize() default 16L;

    long ohcDuration() default 10L;

    long timeout() default 5L;

    long emptyTimeout() default 1L;
}