package com.github.bourbon.springframework.boot.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 17:44
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage {

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};
}