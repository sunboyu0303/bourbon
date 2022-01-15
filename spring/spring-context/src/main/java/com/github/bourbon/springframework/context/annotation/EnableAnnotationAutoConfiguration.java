package com.github.bourbon.springframework.context.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/23 17:41
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableAnnotationAutoConfigurationRegistrar.class)
public @interface EnableAnnotationAutoConfiguration {

    Class<? extends Annotation>[] annotations();

    String[] basePackages();

    String[] location() default {};
}