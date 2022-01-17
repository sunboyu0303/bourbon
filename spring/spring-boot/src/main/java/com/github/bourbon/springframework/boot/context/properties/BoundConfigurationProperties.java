package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationProperty;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 19:41
 */
public class BoundConfigurationProperties {

    private Map<ConfigurationPropertyName, ConfigurationProperty> properties = MapUtils.newLinkedHashMap();

    private static final String BEAN_NAME = BoundConfigurationProperties.class.getName();

    void add(ConfigurationProperty configurationProperty) {
        properties.put(configurationProperty.getName(), configurationProperty);
    }

    public ConfigurationProperty get(ConfigurationPropertyName name) {
        return properties.get(name);
    }

    public Map<ConfigurationPropertyName, ConfigurationProperty> getAll() {
        return MapUtils.unmodifiableMap(properties);
    }

    public static BoundConfigurationProperties get(ApplicationContext context) {
        return BooleanUtils.defaultIfFalse(context.containsBeanDefinition(BEAN_NAME), () -> context.getBean(BEAN_NAME, BoundConfigurationProperties.class));
    }

    static void register(BeanDefinitionRegistry registry) {
        Assert.notNull(registry, "Registry must not be null");
        if (!registry.containsBeanDefinition(BEAN_NAME)) {
            registry.registerBeanDefinition(BEAN_NAME, BeanDefinitionBuilder.genericBeanDefinition(BoundConfigurationProperties.class, BoundConfigurationProperties::new).setRole(BeanDefinition.ROLE_INFRASTRUCTURE).getBeanDefinition());
        }
    }
}