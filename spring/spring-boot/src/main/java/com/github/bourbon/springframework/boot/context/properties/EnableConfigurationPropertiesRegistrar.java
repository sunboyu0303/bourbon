package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.springframework.boot.validation.beanvalidation.MethodValidationExcludeFilter;
import com.github.bourbon.springframework.context.annotation.InfrastructureBeans;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Conventions;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 18:19
 */
class EnableConfigurationPropertiesRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String METHOD_VALIDATION_EXCLUDE_FILTER_BEAN_NAME = Conventions.getQualifiedAttributeName(
            EnableConfigurationPropertiesRegistrar.class, "methodValidationExcludeFilter"
    );

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registerInfrastructureBeans(registry);
        registerMethodValidationExcludeFilter(registry);
        ConfigurationPropertiesBeanRegistrar beanRegistrar = new ConfigurationPropertiesBeanRegistrar(registry);
        getTypes(metadata).forEach(beanRegistrar::register);
    }

    private Set<Class<?>> getTypes(AnnotationMetadata metadata) {
        return metadata.getAnnotations().stream(EnableConfigurationProperties.class).flatMap(a -> Arrays.stream(a.getClassArray(MergedAnnotation.VALUE))).filter(type -> void.class != type).collect(Collectors.toSet());
    }

    static void registerInfrastructureBeans(BeanDefinitionRegistry registry) {
        ConfigurationPropertiesBindingPostProcessor.register(registry);
        BoundConfigurationProperties.register(registry);
        InfrastructureBeans.register(registry);
    }

    static void registerMethodValidationExcludeFilter(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(METHOD_VALIDATION_EXCLUDE_FILTER_BEAN_NAME)) {
            registry.registerBeanDefinition(METHOD_VALIDATION_EXCLUDE_FILTER_BEAN_NAME, BeanDefinitionBuilder.genericBeanDefinition(MethodValidationExcludeFilter.class, () -> MethodValidationExcludeFilter.byAnnotation(ConfigurationProperties.class)).setRole(BeanDefinition.ROLE_INFRASTRUCTURE).getBeanDefinition());
        }
    }
}