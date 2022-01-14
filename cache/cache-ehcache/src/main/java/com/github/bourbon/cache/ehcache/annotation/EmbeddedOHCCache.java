package com.github.bourbon.cache.ehcache.annotation;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 17:02
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EmbeddedOHCCache {

    String timeUnit() default "MINUTES";

    String threadPoolSize() default "8";

    String applyCache() default "true";

    String memoryUnit() default "MB";

    String memorySize() default "16";

    String ohcDuration() default "10";

    String timeout() default "5";

    String emptyTimeout() default "1";
}