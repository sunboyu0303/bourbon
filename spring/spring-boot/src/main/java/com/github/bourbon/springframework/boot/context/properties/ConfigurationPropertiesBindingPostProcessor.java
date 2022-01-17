package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.lang.Assert;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 18:22
 */
public class ConfigurationPropertiesBindingPostProcessor implements BeanPostProcessor, PriorityOrdered, ApplicationContextAware, InitializingBean {

    public static final String BEAN_NAME = ConfigurationPropertiesBindingPostProcessor.class.getName();

    private ApplicationContext applicationContext;

    private BeanDefinitionRegistry registry;

    private ConfigurationPropertiesBinder binder;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        this.registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
        this.binder = ConfigurationPropertiesBinder.get(applicationContext);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        bind(ConfigurationPropertiesBean.get(applicationContext, bean, beanName));
        return bean;
    }

    private void bind(ConfigurationPropertiesBean bean) {
        if (bean == null || hasBoundValueObject(bean.getName())) {
            return;
        }
        Assert.isTrue(bean.getBindMethod() == ConfigurationPropertiesBean.BindMethod.JAVA_BEAN,
                "Cannot bind @ConfigurationProperties for bean '" + bean.getName() + "'. Ensure that @ConstructorBinding has not been applied to regular bean");
        try {
            binder.bind(bean);
        } catch (Exception ex) {
            throw new ConfigurationPropertiesBindException(bean, ex);
        }
    }

    private boolean hasBoundValueObject(String beanName) {
        return registry.containsBeanDefinition(beanName) && ConfigurationPropertiesBean.BindMethod.VALUE_OBJECT.equals(registry.getBeanDefinition(beanName).getAttribute(ConfigurationPropertiesBean.BindMethod.class.getName()));
    }

    public static void register(BeanDefinitionRegistry registry) {
        Assert.notNull(registry, "Registry must not be null");
        if (!registry.containsBeanDefinition(BEAN_NAME)) {
            registry.registerBeanDefinition(BEAN_NAME, BeanDefinitionBuilder.rootBeanDefinition(ConfigurationPropertiesBindingPostProcessor.class).setRole(BeanDefinition.ROLE_INFRASTRUCTURE).getBeanDefinition());
        }
        ConfigurationPropertiesBinder.register(registry);
    }
}