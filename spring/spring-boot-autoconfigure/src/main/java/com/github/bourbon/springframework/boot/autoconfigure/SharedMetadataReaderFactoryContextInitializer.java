package com.github.bourbon.springframework.boot.autoconfigure;

import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.springframework.boot.type.classreading.ConcurrentReferenceCachingMetadataReaderFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 12:12
 */
class SharedMetadataReaderFactoryContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    static final String BEAN_NAME = "org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory";

    @Override
    public void initialize(ConfigurableApplicationContext ac) {
        ac.addBeanFactoryPostProcessor(new CachingMetadataReaderFactoryPostProcessor(ac));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    static class CachingMetadataReaderFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

        private final ConfigurableApplicationContext context;

        CachingMetadataReaderFactoryPostProcessor(ConfigurableApplicationContext context) {
            this.context = context;
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        }

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            register(registry);
            configureConfigurationClassPostProcessor(registry);
        }

        private void register(BeanDefinitionRegistry registry) {
            if (!registry.containsBeanDefinition(BEAN_NAME)) {
                registry.registerBeanDefinition(BEAN_NAME, BeanDefinitionBuilder.rootBeanDefinition(SharedMetadataReaderFactoryBean.class, SharedMetadataReaderFactoryBean::new).getBeanDefinition());
            }
        }

        private void configureConfigurationClassPostProcessor(BeanDefinitionRegistry registry) {
            try {
                configureConfigurationClassPostProcessor(registry.getBeanDefinition(AnnotationConfigUtils.CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME));
            } catch (NoSuchBeanDefinitionException e) {
                // ignore
            }
        }

        private void configureConfigurationClassPostProcessor(BeanDefinition definition) {
            if (definition instanceof AbstractBeanDefinition) {
                configureConfigurationClassPostProcessor((AbstractBeanDefinition) definition);
                return;
            }
            configureConfigurationClassPostProcessor(definition.getPropertyValues());
        }

        private void configureConfigurationClassPostProcessor(AbstractBeanDefinition definition) {
            Supplier<?> instanceSupplier = definition.getInstanceSupplier();
            if (instanceSupplier != null) {
                definition.setInstanceSupplier(new ConfigurationClassPostProcessorCustomizingSupplier(context, instanceSupplier));
                return;
            }
            configureConfigurationClassPostProcessor(definition.getPropertyValues());
        }

        private void configureConfigurationClassPostProcessor(MutablePropertyValues propertyValues) {
            propertyValues.add(ClassUtils.lowerFirst(MetadataReaderFactory.class), new RuntimeBeanReference(BEAN_NAME));
        }
    }

    static class ConfigurationClassPostProcessorCustomizingSupplier implements Supplier<Object> {

        private final ConfigurableApplicationContext context;

        private final Supplier<?> instanceSupplier;

        ConfigurationClassPostProcessorCustomizingSupplier(ConfigurableApplicationContext context, Supplier<?> instanceSupplier) {
            this.context = context;
            this.instanceSupplier = instanceSupplier;
        }

        @Override
        public Object get() {
            Object instance = instanceSupplier.get();
            if (instance instanceof ConfigurationClassPostProcessor) {
                configureConfigurationClassPostProcessor((ConfigurationClassPostProcessor) instance);
            }
            return instance;
        }

        private void configureConfigurationClassPostProcessor(ConfigurationClassPostProcessor instance) {
            instance.setMetadataReaderFactory(context.getBean(BEAN_NAME, MetadataReaderFactory.class));
        }
    }

    static class SharedMetadataReaderFactoryBean implements FactoryBean<ConcurrentReferenceCachingMetadataReaderFactory>, BeanClassLoaderAware, ApplicationListener<ContextRefreshedEvent> {

        private ConcurrentReferenceCachingMetadataReaderFactory metadataReaderFactory;

        @Override
        public void setBeanClassLoader(ClassLoader classLoader) {
            metadataReaderFactory = new ConcurrentReferenceCachingMetadataReaderFactory(classLoader);
        }

        @Override
        public ConcurrentReferenceCachingMetadataReaderFactory getObject() {
            return metadataReaderFactory;
        }

        @Override
        public Class<?> getObjectType() {
            return CachingMetadataReaderFactory.class;
        }

        @Override
        public boolean isSingleton() {
            return true;
        }

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            metadataReaderFactory.clearCache();
        }
    }
}