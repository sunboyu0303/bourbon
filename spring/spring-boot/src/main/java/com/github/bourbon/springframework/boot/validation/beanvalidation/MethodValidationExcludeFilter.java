package com.github.bourbon.springframework.boot.validation.beanvalidation;

import org.springframework.core.annotation.MergedAnnotations;

import java.lang.annotation.Annotation;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 18:26
 */
public interface MethodValidationExcludeFilter {

    boolean isExcluded(Class<?> type);

    static MethodValidationExcludeFilter byAnnotation(Class<? extends Annotation> annotationType) {
        return byAnnotation(annotationType, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS);
    }

    static MethodValidationExcludeFilter byAnnotation(Class<? extends Annotation> annotationType, MergedAnnotations.SearchStrategy searchStrategy) {
        return t -> MergedAnnotations.from(t, MergedAnnotations.SearchStrategy.SUPERCLASS).isPresent(annotationType);
    }
}