package com.github.bourbon.base.lang;

import com.github.bourbon.base.lang.cache.SimpleCache;

import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 10:49
 */
public final class Singleton {

    private static final SimpleCache<Class<?>, Object> POOL = new SimpleCache<>();

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> c) {
        return (T) POOL.get(c);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> c, Supplier<T> s) {
        return (T) POOL.get(c, s::get);
    }

    public static <T> void put(T t) {
        POOL.put(t.getClass(), t);
    }

    public static <T> void put(Class<T> c, T t) {
        POOL.put(c, t);
    }

    public static <T> void remove(Class<T> c) {
        POOL.remove(c);
    }

    public static void destroy() {
        POOL.clear();
    }

    private Singleton() {
    }
}