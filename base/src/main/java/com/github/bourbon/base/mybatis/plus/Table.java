package com.github.bourbon.base.mybatis.plus;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {

    String selectDataSource();

    String selectTable();

    int selectDataSourceSize();

    int selectTableSize();
}