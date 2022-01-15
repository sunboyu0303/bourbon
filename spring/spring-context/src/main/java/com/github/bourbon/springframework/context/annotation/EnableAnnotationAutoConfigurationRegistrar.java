package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/23 18:14
 */
class EnableAnnotationAutoConfigurationRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        try {
            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false);
            EnableAnnotationAutoConfiguration annotation = AnnotationHelperUtils.getAnnotation(metadata, EnableAnnotationAutoConfiguration.class);
            Arrays.stream(annotation.annotations()).forEach(c -> scanner.addIncludeFilter(new AnnotationTypeFilter(c)));
            scanner.scan(annotation.basePackages());

            XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            for (String s : annotation.location()) {
                reader.loadBeanDefinitions(resolver.getResources(s));
            }

            InfrastructureBeans.register(registry);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}