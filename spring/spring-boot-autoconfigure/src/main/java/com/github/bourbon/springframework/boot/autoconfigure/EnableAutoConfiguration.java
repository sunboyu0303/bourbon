package com.github.bourbon.springframework.boot.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 00:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {

    String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

    Class<?>[] exclude() default {};

    String[] excludeName() default {};
}