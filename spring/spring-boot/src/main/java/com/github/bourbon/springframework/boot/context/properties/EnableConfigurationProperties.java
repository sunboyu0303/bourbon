package com.github.bourbon.springframework.boot.context.properties;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 18:18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableConfigurationPropertiesRegistrar.class)
public @interface EnableConfigurationProperties {

    String VALIDATOR_BEAN_NAME = "configurationPropertiesValidator";

    Class<?>[] value() default {};
}