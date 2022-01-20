package com.github.bourbon.springframework.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.github.bourbon.springframework.beans.factory.config.BeanDefinition;
import com.github.bourbon.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/8 10:05
 */
class ClassPathScanningCandidateComponentProvider {

    Set<BeanDefinition> findCandidateComponents(String basePackage) {
        return ClassUtil.scanPackageByAnnotation(basePackage, Component.class).stream().map(BeanDefinition::of).collect(Collectors.toSet());
    }
}