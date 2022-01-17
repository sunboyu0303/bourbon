package com.github.bourbon.springframework.boot.autoconfigure;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/24 11:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface AutoConfigureOrder {

    int DEFAULT_ORDER = 0;

    int value() default DEFAULT_ORDER;
}