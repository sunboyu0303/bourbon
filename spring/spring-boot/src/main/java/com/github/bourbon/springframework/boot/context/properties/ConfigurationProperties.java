package com.github.bourbon.springframework.boot.context.properties;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 17:04
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ConfigurationProperties {

    @AliasFor("prefix")
    String value() default "";

    @AliasFor("value")
    String prefix() default "";

    boolean ignoreInvalidFields() default false;

    boolean ignoreUnknownFields() default true;
}