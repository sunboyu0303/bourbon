package com.github.bourbon.cache.guava.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 22:09
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JVMCache {

    long maximumSize() default 1024L;

    long jvmDuration() default 1L;

    int concurrencyLevel() default 4;

    int initialCapacity() default 1024;

    TimeUnit timeUnit() default TimeUnit.MINUTES;

    int threadPoolSize() default 8;
    
    boolean applyCache() default true;
}