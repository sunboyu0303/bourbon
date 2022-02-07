package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 22:49
 */
public final class PropertiesBeanUtils implements InitializingBean {

    private static final String BEAN_NAME = PropertiesBeanUtils.class.getName();

    @Value("classpath:**/*.properties")
    private Resource[] properties;

    @Value("classpath:**/*.yml")
    private Resource[] yamlProperties;

    private Properties prop;

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        return ObjectUtils.defaultIfNullElseFunction(prop.get(key), Object::toString, defaultValue);
    }

    public Boolean getBoolean(String key) {
        return Boolean.valueOf(getProperty(key));
    }

    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return ObjectUtils.defaultIfNullElseFunction(getProperty(key), Integer::parseInt, defaultValue);
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public Long getLong(String key, Long defaultValue) {
        return ObjectUtils.defaultIfNullElseFunction(getProperty(key), Long::parseLong, defaultValue);
    }

    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    public Double getDouble(String key, Double defaultValue) {
        return ObjectUtils.defaultIfNullElseFunction(getProperty(key), Double::parseDouble, defaultValue);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        prop = new Properties();

        for (Resource resource : properties) {
            PropertiesLoaderUtils.fillProperties(prop, resource);
        }

        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(yamlProperties);
        if (ObjectUtils.nonNull(yaml.getObject())) {
            prop.putAll(yaml.getObject());
        }
    }

    static void register(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(BEAN_NAME)) {
            registry.registerBeanDefinition(BEAN_NAME, BeanDefinitionBuilder.genericBeanDefinition(PropertiesBeanUtils.class, PropertiesBeanUtils::new).getBeanDefinition());
        }
    }
}