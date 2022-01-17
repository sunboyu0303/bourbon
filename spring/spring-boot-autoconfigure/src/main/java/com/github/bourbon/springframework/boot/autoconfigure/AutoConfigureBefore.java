package com.github.bourbon.springframework.boot.autoconfigure;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/24 11:35
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface AutoConfigureBefore {
    
    Class<?>[] value() default {};

    String[] name() default {};
}