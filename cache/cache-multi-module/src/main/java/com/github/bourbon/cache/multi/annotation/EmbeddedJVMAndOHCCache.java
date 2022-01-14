package com.github.bourbon.cache.multi.annotation;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 17:03
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EmbeddedJVMAndOHCCache {

    String maximumSize() default "1024";

    String jvmDuration() default "1";

    String initialCapacity() default "1024";

    String timeUnit() default "MINUTES";

    String async() default "false";

    String threadPoolSize() default "8";

    String applyCache() default "true";

    String memoryUnit() default "MB";

    String memorySize() default "16";

    String ohcDuration() default "10";

    String timeout() default "5";

    String emptyTimeout() default "1";
}