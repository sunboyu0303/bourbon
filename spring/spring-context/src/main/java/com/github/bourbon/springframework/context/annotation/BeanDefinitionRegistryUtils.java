package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 22:58
 */
public class BeanDefinitionRegistryUtils implements ApplicationContextAware {

    private static final String BEAN_NAME = BeanDefinitionRegistryUtils.class.getName();

    private BeanDefinitionRegistry registry;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
    }

    public boolean removeBeanDefinition(String name) {
        return BooleanUtils.defaultIfFalse(containsBeanDefinition(name), () -> {
            registry.removeBeanDefinition(name);
            return true;
        }, false);
    }

    public boolean registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        return BooleanUtils.defaultIfFalse(!containsBeanDefinition(name), () -> {
            registry.registerBeanDefinition(name, beanDefinition);
            return true;
        }, false);
    }

    public boolean containsBeanDefinition(String name) {
        return registry.containsBeanDefinition(name);
    }

    public BeanDefinition getBeanDefinition(String name) {
        return BooleanUtils.defaultIfPredicate(name, this::containsBeanDefinition, registry::getBeanDefinition);
    }

    public boolean registerBeanDefinitionIfNotExists(String beanName, Class<?> beanClass) {
        return registerBeanDefinitionIfNotExists(beanName, beanClass, null);
    }

    public boolean registerBeanDefinitionIfNotExists(String beanName, Class<?> beanClass, Map<String, Object> propertyValues) {
        return BooleanUtils.defaultIfFalse(!registry.containsBeanDefinition(beanName), () -> {
            for (String candidate : registry.getBeanDefinitionNames()) {
                if (ObjectUtils.equals(registry.getBeanDefinition(candidate).getBeanClassName(), beanClass.getName())) {
                    return false;
                }
            }
            BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
            if (!MapUtils.isEmpty(propertyValues)) {
                propertyValues.forEach((k, v) -> beanDefinition.getPropertyValues().add(k, v));
            }
            registry.registerBeanDefinition(beanName, beanDefinition);
            return true;
        }, false);
    }

    static void register(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(BEAN_NAME)) {
            registry.registerBeanDefinition(BEAN_NAME, BeanDefinitionBuilder.genericBeanDefinition(BeanDefinitionRegistryUtils.class, BeanDefinitionRegistryUtils::new).getBeanDefinition());
        }
    }
}