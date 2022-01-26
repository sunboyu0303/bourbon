package com.github.bourbon.base.utils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 15:05
 */
public interface ClassLoaderUtils {

    static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    static ClassLoader getReferrerClassLoader(Class<?> referrer) {
        ClassLoader classLoader = null;
        if (referrer != null) {
            classLoader = referrer.getClassLoader();
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }
        return classLoader;
    }

    static ClassLoader getCallerClassLoader() {
        try {
            String callerClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            try {
                return Class.forName(callerClassName).getClassLoader();
            } catch (ClassNotFoundException e) {
                return Class.forName(callerClassName, true, getContextClassLoader()).getClassLoader();
            }
        } catch (Throwable t) {
            throw new RuntimeException("Failed to get caller classloader ", t);
        }
    }

    static ClassLoader getClassLoader() {
        return getClassLoader(ClassLoaderUtils.class);
    }

    static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            // ignore
        }
        if (cl == null) {
            cl = clazz.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return cl;
    }
}