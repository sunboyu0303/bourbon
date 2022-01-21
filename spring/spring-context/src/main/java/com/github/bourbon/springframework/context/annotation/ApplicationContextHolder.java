package com.github.bourbon.springframework.context.annotation;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/8 18:09
 */
public final class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.CONTEXT = applicationContext;
    }

    public static Environment getEnvironment() {
        return CONTEXT.getEnvironment();
    }

    public static boolean containsBeanDefinition(String beanName) {
        return CONTEXT.containsBeanDefinition(beanName);
    }

    public static String[] getBeanDefinitionNames() {
        return CONTEXT.getBeanDefinitionNames();
    }

    public static String[] getBeanNamesForType(ResolvableType type) {
        return CONTEXT.getBeanNamesForType(type);
    }

    public static String[] getBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
        return CONTEXT.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }

    public static String[] getBeanNamesForType(Class<?> type) {
        return CONTEXT.getBeanNamesForType(type);
    }

    public static String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return CONTEXT.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }

    public static <T> T getBean(Class<T> clazz) {
        return CONTEXT.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return CONTEXT.getBean(name, clazz);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return CONTEXT.getBeansOfType(clazz);
    }

    public static <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return CONTEXT.findAnnotationOnBean(beanName, annotationType);
    }
}