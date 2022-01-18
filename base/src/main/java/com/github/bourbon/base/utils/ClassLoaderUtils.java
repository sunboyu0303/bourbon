package com.github.bourbon.base.utils;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 15:05
 */
public interface ClassLoaderUtils {

    Logger LOGGER = LoggerFactory.getLogger(ClassLoaderUtils.class);

    static ClassLoader getClassLoader() {
        return getClassLoader(ClassLoaderUtils.class);
    }

    static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            LOGGER.error(e);
        }
        if (cl == null) {
            cl = clazz.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        }
        return cl;
    }
}