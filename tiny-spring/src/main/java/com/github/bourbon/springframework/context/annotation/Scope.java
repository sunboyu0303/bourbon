package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/8 10:02
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    ConfigurableBeanFactory.Scope value() default ConfigurableBeanFactory.Scope.SINGLETON;
}