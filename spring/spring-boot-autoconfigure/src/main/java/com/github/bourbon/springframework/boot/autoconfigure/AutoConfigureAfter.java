package com.github.bourbon.springframework.boot.autoconfigure;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/24 11:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AutoConfigureAfter {

    Class<?>[] value() default {};

    String[] name() default {};
}