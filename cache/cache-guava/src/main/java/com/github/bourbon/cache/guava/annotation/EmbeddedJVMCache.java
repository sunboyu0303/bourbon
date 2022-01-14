package com.github.bourbon.cache.guava.annotation;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 22:12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EmbeddedJVMCache {

    String maximumSize() default "1024";

    String jvmDuration() default "1";

    String concurrencyLevel() default "4";

    String initialCapacity() default "1024";

    String timeUnit() default "MINUTES";

    String threadPoolSize() default "8";

    String applyCache() default "true";
}