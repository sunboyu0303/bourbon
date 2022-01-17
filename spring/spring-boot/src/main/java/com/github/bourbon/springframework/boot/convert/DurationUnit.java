package com.github.bourbon.springframework.boot.convert;

import java.lang.annotation.*;
import java.time.temporal.ChronoUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 11:19
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DurationUnit {
    
    ChronoUnit value();
}