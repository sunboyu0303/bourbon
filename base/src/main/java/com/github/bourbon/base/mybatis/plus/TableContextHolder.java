package com.github.bourbon.base.mybatis.plus;

public class TableContextHolder {

    private static final ThreadLocal<Integer> contextHolder = new ThreadLocal<>();

    public static Integer getTable() {
        return contextHolder.get();
    }

    public static void setTable(Integer table) {
        contextHolder.set(table);
    }

    public static void remove() {
        contextHolder.remove();
    }
}