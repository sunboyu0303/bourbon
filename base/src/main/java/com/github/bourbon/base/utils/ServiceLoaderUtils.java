package com.github.bourbon.base.utils;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 15:03
 */
public final class ServiceLoaderUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLoaderUtils.class);

    public static <T> T loadFirstAvailable(Class<T> clazz) {
        for (T t : load(clazz)) {
            try {
                return t;
            } catch (ServiceConfigurationError e) {
                LOGGER.error(e);
            }
        }
        return null;
    }

    public static <T> T loadFirst(Class<T> clazz) {
        return BooleanUtils.defaultIfPredicate(load(clazz).iterator(), Iterator::hasNext, Iterator::next);
    }

    public static <T> ServiceLoader<T> load(Class<T> clazz) {
        return load(clazz, null);
    }

    public static <T> ServiceLoader<T> load(Class<T> clazz, ClassLoader loader) {
        return ServiceLoader.load(clazz, ObjectUtils.defaultIfNull(loader, ClassLoaderUtils.getClassLoader()));
    }

    public static <T> List<T> loadList(Class<T> clazz) {
        return loadList(clazz, null);
    }

    public static <T> List<T> loadList(Class<T> clazz, ClassLoader loader) {
        return ListUtils.newList(false, load(clazz, loader));
    }

    private ServiceLoaderUtils() {
    }
}