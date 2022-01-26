package com.github.bourbon.base.mybatis.plus;

public class DbContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static String getDb() {
        return contextHolder.get();
    }

    public static void setDb(String db) {
        contextHolder.set(db);
    }

    public static void remove() {
        contextHolder.remove();
    }
}