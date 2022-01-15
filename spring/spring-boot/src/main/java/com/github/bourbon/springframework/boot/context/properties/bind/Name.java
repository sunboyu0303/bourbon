package com.github.bourbon.springframework.boot.context.properties.bind;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 11:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface Name {

    String value();
}