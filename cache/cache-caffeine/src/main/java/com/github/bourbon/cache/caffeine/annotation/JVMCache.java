package com.github.bourbon.cache.caffeine.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 17:00
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JVMCache {

    long maximumSize() default 1024L;

    long jvmDuration() default 1L;

    int initialCapacity() default 1024;

    TimeUnit timeUnit() default TimeUnit.MINUTES;

    boolean async() default false;

    int threadPoolSize() default 8;

    boolean applyCache() default true;
}