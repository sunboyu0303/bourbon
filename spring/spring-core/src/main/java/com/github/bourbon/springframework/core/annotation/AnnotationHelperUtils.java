package com.github.bourbon.springframework.core.annotation;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/16 17:03
 */
public interface AnnotationHelperUtils {

    static <T extends Annotation> T getAnnotation(AnnotatedElement element, Class<T> annotationType) {
        return AnnotatedElementUtils.findMergedAnnotation(element, annotationType);
    }

    static AnnotationAttributes getAnnotationAttributes(AnnotatedElement e, Class<? extends Annotation> clazz) {
        return getAnnotationAttributes(e, clazz, false, false);
    }

    static AnnotationAttributes getAnnotationAttributes(AnnotatedElement e, Class<? extends Annotation> clazz, boolean asString, boolean asMap) {
        return AnnotatedElementUtils.findMergedAnnotationAttributes(e, clazz, asString, asMap);
    }

    static AnnotationAttributes getAnnotationAttributes(AnnotatedTypeMetadata metadata, Class<? extends Annotation> clazz) {
        return getAnnotationAttributes(metadata, clazz, false);
    }

    static AnnotationAttributes getAnnotationAttributes(AnnotatedTypeMetadata metadata, Class<? extends Annotation> clazz, boolean asString) {
        return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(clazz.getName(), asString));
    }
}