package com.github.bourbon.cache.caffeine.annotation;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 17:00
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EmbeddedJVMCache {

    String maximumSize() default "1024";

    String jvmDuration() default "1";

    String initialCapacity() default "1024";

    String timeUnit() default "MINUTES";

    String async() default "false";

    String threadPoolSize() default "8";

    String applyCache() default "true";
}