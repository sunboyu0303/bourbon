package com.github.bourbon.springframework.boot.context.properties;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 20:03
 */
@Qualifier(ConfigurationPropertiesBinding.VALUE)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigurationPropertiesBinding {

    String VALUE = "com.github.bourbon.springframework.boot.context.properties.ConfigurationPropertiesBinding";
}