package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ClassLoaderUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 16:56
 */
public final class ConditionContextUtils {

    private static final AtomicBoolean initialized = new AtomicBoolean();

    private static ConditionContext conditionContext;

    public static ConditionContext getConditionContext() {
        return conditionContext;
    }

    static void init(BeanDefinitionRegistry registry) {
        if (initialized.compareAndSet(false, true)) {
            conditionContext = new ConditionContextImpl(registry);
        }
    }

    private ConditionContextUtils() {
    }

    private static class ConditionContextImpl implements ConditionContext {

        private final BeanDefinitionRegistry registry;

        private final ConfigurableListableBeanFactory beanFactory;

        private final Environment environment;

        private final ResourceLoader resourceLoader;

        private final ClassLoader classLoader;

        private ConditionContextImpl(BeanDefinitionRegistry registry) {
            Assert.notNull(registry, "No BeanDefinitionRegistry available");
            this.registry = registry;
            this.beanFactory = deduceBeanFactory(registry);
            this.environment = deduceEnvironment(registry);
            this.resourceLoader = deduceResourceLoader(registry);
            this.classLoader = deduceClassLoader(resourceLoader, beanFactory);
        }

        private ConfigurableListableBeanFactory deduceBeanFactory(BeanDefinitionRegistry registry) {
            return BooleanUtils.defaultSupplierIfAssignableFrom(registry, ConfigurableListableBeanFactory.class, ConfigurableListableBeanFactory.class::cast,
                    () -> BooleanUtils.defaultIfAssignableFrom(registry, ConfigurableApplicationContext.class, r -> ((ConfigurableApplicationContext) r).getBeanFactory())
            );
        }

        private Environment deduceEnvironment(BeanDefinitionRegistry registry) {
            return BooleanUtils.defaultSupplierIfAssignableFrom(registry, EnvironmentCapable.class, r -> ((EnvironmentCapable) r).getEnvironment(), StandardEnvironment::new);
        }

        private ResourceLoader deduceResourceLoader(BeanDefinitionRegistry registry) {
            return BooleanUtils.defaultSupplierIfAssignableFrom(registry, ResourceLoader.class, ResourceLoader.class::cast, DefaultResourceLoader::new);
        }

        private ClassLoader deduceClassLoader(ResourceLoader resourceLoader, ConfigurableBeanFactory beanFactory) {
            if (resourceLoader != null) {
                ClassLoader loader = resourceLoader.getClassLoader();
                if (loader != null) {
                    return loader;
                }
            }
            return ObjectUtils.defaultSupplierIfNullElseFunction(beanFactory, ConfigurableBeanFactory::getBeanClassLoader, ClassLoaderUtils::getClassLoader);
        }

        @Override
        public BeanDefinitionRegistry getRegistry() {
            return registry;
        }

        @Override
        public ConfigurableListableBeanFactory getBeanFactory() {
            return beanFactory;
        }

        @Override
        public Environment getEnvironment() {
            return environment;
        }

        @Override
        public ResourceLoader getResourceLoader() {
            return resourceLoader;
        }

        @Override
        public ClassLoader getClassLoader() {
            return classLoader;
        }
    }
}