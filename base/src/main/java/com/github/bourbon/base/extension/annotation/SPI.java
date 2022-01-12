package com.github.bourbon.base.extension.annotation;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 11:24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    String value() default "";

    ExtensionScope scope() default ExtensionScope.APPLICATION;
}