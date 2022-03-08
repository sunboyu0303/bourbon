package com.github.bourbon.pfinder.profiler.service.tribe.annotation;

import com.github.bourbon.pfinder.profiler.service.tribe.OrderedServiceTribe;
import com.github.bourbon.pfinder.profiler.service.tribe.ServiceTribeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 15:04
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ServiceTribeType(OrderedServiceTribe.Factory.class)
public @interface OrderedTribe {

    Class<? extends Comparator> value();

    boolean chiefInHead() default true;
}