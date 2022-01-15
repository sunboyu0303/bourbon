package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 23:46
 */
public final class ApplicationContextUtils {

    private static final AtomicBoolean initialized = new AtomicBoolean();

    private static ApplicationContext ac;

    private static Environment environment;

    public static Object getBean(String name) {
        return ac.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return ac.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return ac.getBean(name, clazz);
    }

    public static String[] getBeanNameByType(Class<?> clazz) {
        return ac.getBeanNamesForType(clazz);
    }

    public static String[] getBeanDefinitionNames() {
        return ac.getBeanDefinitionNames();
    }

    public static List<Object> getBeansWithAnnotation(Class<? extends Annotation> clazz) {
        return ListUtils.newArrayList(ac.getBeansWithAnnotation(clazz).values());
    }

    public static <T> Map<String, T> getBeansWithType(Class<T> type) {
        return Arrays.stream(ac.getBeanNamesForType(type)).collect(Collectors.toMap(Function.identity(), n -> ac.getBean(n, type)));
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        return ObjectUtils.defaultIfNull(environment.getProperty(key), Function.identity(), defaultValue);
    }

    public static Boolean getBoolean(String key) {
        return Boolean.valueOf(getProperty(key));
    }

    public static Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public static Integer getInteger(String key, Integer defaultValue) {
        return ObjectUtils.defaultIfNull(getProperty(key), Integer::parseInt, defaultValue);
    }

    public static Long getLong(String key) {
        return getLong(key, null);
    }

    public static Long getLong(String key, Long defaultValue) {
        return ObjectUtils.defaultIfNull(getProperty(key), Long::parseLong, defaultValue);
    }

    public static Double getDouble(String key) {
        return getDouble(key, null);
    }

    public static Double getDouble(String key, Double defaultValue) {
        return ObjectUtils.defaultIfNull(getProperty(key), Double::parseDouble, defaultValue);
    }

    static void init(BeanDefinitionRegistry registry) {
        if (initialized.compareAndSet(false, true)) {
            ac = BooleanUtils.defaultIfAssignableFrom(registry, ConfigurableApplicationContext.class, ConfigurableApplicationContext.class::cast);
            environment = ObjectUtils.defaultIfNull(ac, EnvironmentCapable::getEnvironment);
        }
    }

    private ApplicationContextUtils() {
    }
}