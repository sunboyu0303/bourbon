package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 18:47
 */
final class ConfigurationPropertiesBeanRegistrar {

    private final BeanDefinitionRegistry registry;
    private final BeanFactory beanFactory;

    ConfigurationPropertiesBeanRegistrar(BeanDefinitionRegistry registry) {
        this.registry = registry;
        this.beanFactory = (BeanFactory) registry;
    }

    void register(Class<?> type) {
        register(type, MergedAnnotations.from(type, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ConfigurationProperties.class));
    }

    void register(Class<?> type, MergedAnnotation<ConfigurationProperties> annotation) {
        String name = getName(type, annotation);
        if (!containsBeanDefinition(name)) {
            registerBeanDefinition(name, type, annotation);
        }
    }

    private String getName(Class<?> type, MergedAnnotation<ConfigurationProperties> annotation) {
        return BooleanUtils.defaultIfPredicate(
                BooleanUtils.defaultIfFalse(annotation.isPresent(), () -> annotation.getString("prefix"), StringConstants.EMPTY),
                p -> !CharSequenceUtils.isEmpty(p), p -> p + StringConstants.HYPHEN + type.getName(), type.getName()
        );
    }

    private boolean containsBeanDefinition(String name) {
        return containsBeanDefinition(beanFactory, name);
    }

    private boolean containsBeanDefinition(BeanFactory beanFactory, String name) {
        if (beanFactory instanceof ListableBeanFactory && ((ListableBeanFactory) beanFactory).containsBeanDefinition(name)) {
            return true;
        }
        return BooleanUtils.defaultIfAssignableFrom(beanFactory, HierarchicalBeanFactory.class, bf -> containsBeanDefinition(((HierarchicalBeanFactory) bf).getParentBeanFactory(), name), false);
    }

    private void registerBeanDefinition(String beanName, Class<?> type, MergedAnnotation<ConfigurationProperties> annotation) {
        Assert.isTrue(annotation.isPresent(), () -> "No " + ConfigurationProperties.class.getSimpleName() +
                " annotation found on  '" + type.getName() + "'.");
        registry.registerBeanDefinition(beanName, createBeanDefinition(beanName, type));
    }

    private BeanDefinition createBeanDefinition(String beanName, Class<?> type) {
        ConfigurationPropertiesBean.BindMethod bindMethod = ConfigurationPropertiesBean.BindMethod.forType(type);
        RootBeanDefinition definition = new RootBeanDefinition(type);
        definition.setAttribute(ConfigurationPropertiesBean.BindMethod.class.getName(), bindMethod);
        if (bindMethod == ConfigurationPropertiesBean.BindMethod.VALUE_OBJECT) {
            definition.setInstanceSupplier(() -> createValueObject(beanName, type));
        }
        return definition;
    }

    private Object createValueObject(String beanName, Class<?> beanType) {
        ConfigurationPropertiesBean bean = ConfigurationPropertiesBean.forValueObject(beanType, beanName);
        try {
            return ConfigurationPropertiesBinder.get(beanFactory).bindOrCreate(bean);
        } catch (Exception ex) {
            throw new ConfigurationPropertiesBindException(bean, ex);
        }
    }
}