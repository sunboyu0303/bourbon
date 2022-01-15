package com.github.bourbon.springframework.boot.context.annotation;

import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 17:50
 */
@FunctionalInterface
public interface DeterminableImports {

    Set<Object> determineImports(AnnotationMetadata metadata);
}