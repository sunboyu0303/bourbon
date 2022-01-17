package com.github.bourbon.springframework.boot.convert;

import org.springframework.util.unit.DataUnit;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 12:20
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSizeUnit {

    DataUnit value();
}