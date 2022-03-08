package com.github.bourbon.pfinder.profiler.service.tribe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 19:42
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceTribeType {
    
    Class<? extends ServiceTribeFactory> value();
}