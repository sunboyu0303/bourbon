package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.github.bourbon.springframework.beans.factory.config.BeanDefinition;
import com.github.bourbon.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.github.bourbon.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.github.bourbon.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/8 10:08
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        Arrays.stream(basePackages).forEach(basePackage -> findCandidateComponents(basePackage).forEach(beanDefinition -> {
            ConfigurableBeanFactory.Scope beanScope = resolveBeanScope(beanDefinition);
            if (!ObjectUtils.isNull(beanScope)) {
                beanDefinition.setScope(beanScope);
            }
            registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
        }));

        Class<?> clazz = AutowiredAnnotationBeanPostProcessor.class;
        registry.registerBeanDefinition(clazz.getName(), BeanDefinition.of(clazz));
    }

    private ConfigurableBeanFactory.Scope resolveBeanScope(BeanDefinition beanDefinition) {
        return ObjectUtils.defaultIfNullElseFunction(beanDefinition.getClazz().getAnnotation(Scope.class), Scope::value);
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getClazz();
        String value = clazz.getAnnotation(Component.class).value();
        return CharSequenceUtils.isEmpty(value) ? CharSequenceUtils.lowerFirst(clazz.getSimpleName()) : value;
    }
}