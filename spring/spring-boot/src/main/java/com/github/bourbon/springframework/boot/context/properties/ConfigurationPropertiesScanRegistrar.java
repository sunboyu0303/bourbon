package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.springframework.boot.context.TypeExcludeFilter;
import com.github.bourbon.springframework.context.annotation.InfrastructureBeans;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 18:37
 */
class ConfigurationPropertiesScanRegistrar implements ImportBeanDefinitionRegistrar {

    private final Environment environment;

    private final ResourceLoader resourceLoader;

    ConfigurationPropertiesScanRegistrar(Environment environment, ResourceLoader resourceLoader) {
        this.environment = environment;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        scan(registry, getPackagesToScan(importingClassMetadata));
        InfrastructureBeans.register(registry);
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        ConfigurationPropertiesScan annotation = AnnotationHelperUtils.getAnnotation(metadata, ConfigurationPropertiesScan.class);
        Set<String> packagesToScan = SetUtils.newLinkedHashSet(annotation.basePackages());
        packagesToScan.addAll(Arrays.stream(annotation.basePackageClasses()).map(ClassUtils::getPackageName).collect(Collectors.toSet()));
        if (packagesToScan.isEmpty()) {
            packagesToScan.add(ClassUtils.getPackageName(metadata.getClassName()));
        }
        packagesToScan.removeIf(CharSequenceUtils::isBlank);
        return packagesToScan;
    }

    private void scan(BeanDefinitionRegistry registry, Set<String> packages) {
        ConfigurationPropertiesBeanRegistrar registrar = new ConfigurationPropertiesBeanRegistrar(registry);
        ClassPathScanningCandidateComponentProvider scanner = getScanner(registry);
        for (String basePackage : packages) {
            for (BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
                register(registrar, candidate.getBeanClassName());
            }
        }
    }

    private ClassPathScanningCandidateComponentProvider getScanner(BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.setEnvironment(environment);
        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ConfigurationProperties.class));
        TypeExcludeFilter typeExcludeFilter = new TypeExcludeFilter();
        typeExcludeFilter.setBeanFactory((BeanFactory) registry);
        scanner.addExcludeFilter(typeExcludeFilter);
        return scanner;
    }

    private void register(ConfigurationPropertiesBeanRegistrar registrar, String className) throws LinkageError {
        register(registrar, ClassUtils.forName(className, null));
    }

    private void register(ConfigurationPropertiesBeanRegistrar registrar, Class<?> type) {
        if (!isComponent(type)) {
            registrar.register(type);
        }
    }

    private boolean isComponent(Class<?> type) {
        return MergedAnnotations.from(type, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).isPresent(Component.class);
    }
}