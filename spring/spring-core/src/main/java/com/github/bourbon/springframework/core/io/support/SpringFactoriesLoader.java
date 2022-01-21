package com.github.bourbon.springframework.core.io.support;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/18 19:15
 */
public final class SpringFactoriesLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringFactoriesLoader.class);

    private static final String FACTORIES_RESOURCE_LOCATION = "META-INF/bourbon/spring.factories";

    private static final Map<ClassLoader, MultiValueMap<String, String>> CACHE = new ConcurrentReferenceHashMap<>();

    public static <T> List<T> loadFactories(Class<T> factoryType, ClassLoader classLoader) {
        ObjectUtils.requireNonNull(factoryType, "'factoryType' must not be null");

        final ClassLoader classLoaderToUse = ObjectUtils.defaultSupplierIfNull(classLoader, SpringFactoriesLoader.class::getClassLoader);

        List<String> names = loadFactoryNames(factoryType, classLoaderToUse);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Loaded [" + factoryType.getName() + "] names: " + names);
        }

        List<T> result = names.stream().map(n -> instantiateFactory(n, factoryType, classLoaderToUse)).collect(Collectors.toList());

        AnnotationAwareOrderComparator.sort(result);

        return result;
    }

    public static List<String> loadFactoryNames(Class<?> factoryType, ClassLoader classLoader) {
        return loadSpringFactories(ObjectUtils.defaultSupplierIfNull(classLoader, SpringFactoriesLoader.class::getClassLoader)).getOrDefault(factoryType.getName(), Collections.emptyList());
    }

    private static Map<String, List<String>> loadSpringFactories(ClassLoader classLoader) {
        return ObjectUtils.defaultSupplierIfNull(CACHE.get(classLoader), () -> {
            MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
            try {
                Enumeration<URL> urls = classLoader.getResources(FACTORIES_RESOURCE_LOCATION);
                while (urls.hasMoreElements()) {
                    Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(urls.nextElement()));
                    for (Map.Entry<Object, Object> e : properties.entrySet()) {
                        String key = e.getKey().toString().trim();
                        for (String name : CharSequenceUtils.commaDelimitedListToStringArray(e.getValue().toString())) {
                            result.add(key, name.trim());
                        }
                    }
                }
                result.replaceAll((k, v) -> v.stream().distinct().collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));
                CACHE.put(classLoader, result);
                return result;
            } catch (IOException e) {
                throw new IllegalArgumentException("Unable to load factories from location [" + FACTORIES_RESOURCE_LOCATION + "]", e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> T instantiateFactory(String className, Class<T> factoryType, ClassLoader classLoader) {
        try {
            Class<?> clazz = ClassUtils.forName(className, classLoader);
            if (!factoryType.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Class [" + className + "] is not assignable to factory type [" + factoryType.getName() + "]");
            } else {
                return (T) ReflectionUtils.accessibleConstructor(clazz).newInstance();
            }
        } catch (Throwable e) {
            throw new IllegalArgumentException("Unable to instantiate factory class [" + className + "] for factory type [" + factoryType.getName() + "]", e);
        }
    }

    private SpringFactoriesLoader() {
    }
}