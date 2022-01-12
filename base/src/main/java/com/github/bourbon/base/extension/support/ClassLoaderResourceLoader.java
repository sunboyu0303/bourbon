package com.github.bourbon.base.extension.support;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.SystemLogger;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.SetUtils;

import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/24 10:03
 */
final class ClassLoaderResourceLoader {

    private static final Logger LOGGER = new SystemLogger();
    private static final Map<ClassLoader, Map<String, Set<URL>>> CACHE_MAP = new ConcurrentHashMap<>();

    static Map<ClassLoader, Set<URL>> loadResources(String fileName, List<ClassLoader> classLoaders) {
        return MapUtils.unmodifiableMap(classLoaders.stream().collect(Collectors.toMap(Function.identity(), c -> loadResources(fileName, c))));
    }

    private static Set<URL> loadResources(String fileName, ClassLoader classLoader) {
        return CACHE_MAP.computeIfAbsent(classLoader, o -> MapUtils.newConcurrentHashMap()).computeIfAbsent(fileName, o -> {
            Set<URL> set = SetUtils.newLinkedHashSet();
            try {
                Enumeration<URL> urls = classLoader.getResources(fileName);
                if (urls != null) {
                    while (urls.hasMoreElements()) {
                        set.add(urls.nextElement());
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e);
            }
            return set;
        });
    }

    private ClassLoaderResourceLoader() {
    }
}