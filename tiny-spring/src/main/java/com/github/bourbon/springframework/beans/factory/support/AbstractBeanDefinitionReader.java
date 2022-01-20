package com.github.bourbon.springframework.beans.factory.support;

import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.io.ResourceLoader;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 00:22
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private final ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, ScopeModelUtils.getExtensionLoader(ResourceLoader.class).getDefaultExtension());
    }

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}