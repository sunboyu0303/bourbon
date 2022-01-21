package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/31 16:53
 */
public class SelectorScanImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        SelectorScan scan = AnnotationHelperUtils.getAnnotation(metadata, SelectorScan.class);
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        Arrays.stream(scan.classes()).forEach(o -> scanner.addIncludeFilter(new AssignableTypeFilter(o)));
        Arrays.stream(scan.classNames()).forEach(o -> scanner.addIncludeFilter(new AssignableTypeFilter(ClassUtils.forName(o))));
        return CollectionUtils.toStringArray(Arrays.stream(scan.basePackages()).flatMap(i -> scanner.findCandidateComponents(i).stream()).map(BeanDefinition::getBeanClassName).collect(Collectors.toSet()));
    }
}