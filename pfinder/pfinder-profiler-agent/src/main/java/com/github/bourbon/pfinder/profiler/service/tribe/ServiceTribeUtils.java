package com.github.bourbon.pfinder.profiler.service.tribe;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 19:42
 */
public class ServiceTribeUtils {

    private static final Map<Class<?>, ServiceTribeFactory> FACTORY_CACHE = new ConcurrentHashMap<>();
    private static final ServiceTribeFactory NOOP_FACTORY = type -> null;

    private ServiceTribeUtils() {
        throw new UnsupportedOperationException();
    }

    public static ServiceTribe tryCreateServiceTribe(Class<?> type) {
        return FACTORY_CACHE.computeIfAbsent(type, c -> {
            ServiceTribeType serviceTribeType = foundServiceTribeType(c);
            if (serviceTribeType == null) {
                return NOOP_FACTORY;
            }
            Class<?> factoryClass = serviceTribeType.value();
            try {
                return (ServiceTribeFactory) factoryClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("create ServiceTribeFactory instance error. factoryClass=" + factoryClass.getName(), e);
            }
        }).build(type);
    }

    private static ServiceTribeType foundServiceTribeType(Class<?> type) {
        for (Annotation annotation : type.getAnnotations()) {
            if (annotation instanceof ServiceTribeType) {
                return (ServiceTribeType) annotation;
            }
            ServiceTribeType serviceTribeType = annotation.annotationType().getAnnotation(ServiceTribeType.class);
            if (serviceTribeType != null) {
                return serviceTribeType;
            }
        }
        return null;
    }
}