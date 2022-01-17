package com.github.bourbon.springframework.boot.context.properties;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 18:15
 */
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConstructorBinding {
}