package com.github.bourbon.pfinder.profiler.sdk.metric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 00:07
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PFMetricTag {

    String value();
}