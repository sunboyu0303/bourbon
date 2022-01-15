package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/31 16:12
 */
class RegistrarScanImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        RegistrarScan scan = AnnotationHelperUtils.getAnnotation(metadata, RegistrarScan.class);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false);
        Arrays.stream(scan.classes()).forEach(o -> scanner.addIncludeFilter(new AssignableTypeFilter(o)));
        Arrays.stream(scan.classNames()).forEach(o -> scanner.addIncludeFilter(new AssignableTypeFilter(ClassUtils.forName(o))));
        scanner.scan(scan.basePackages());
        InfrastructureBeans.register(registry);
    }
}