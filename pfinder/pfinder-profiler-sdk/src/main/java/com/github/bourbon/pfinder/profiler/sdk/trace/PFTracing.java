package com.github.bourbon.pfinder.profiler.sdk.trace;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 17:08
 */
@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface PFTracing {

    /**
     * The name of trace-point，it will be display in you trace & metric dashboard. If let it empty, then will be use the full method-name be the name.
     */
    String name() default "";

    /**
     * custom metric tags. example: key=value; <b>*** Key must start with a lowercase letter and only support lowercase letters, numbers, and '_' ***</b>
     */
    String[] metricTags() default {};
}