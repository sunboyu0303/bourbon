package com.github.bourbon.springframework.context.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/31 16:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SelectorScanImportSelector.class)
public @interface SelectorScan {

    String[] basePackages();

    Class<?>[] classes() default {};

    String[] classNames() default {};
}