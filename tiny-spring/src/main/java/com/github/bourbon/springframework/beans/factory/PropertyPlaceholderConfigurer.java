package com.github.bourbon.springframework.beans.factory;

import com.github.bourbon.base.utils.PropertyPlaceholderUtils;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.PropertyValue;
import com.github.bourbon.springframework.beans.PropertyValues;
import com.github.bourbon.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.github.bourbon.springframework.utils.StringValueResolver;

import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/8 10:17
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    private String location;

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            Properties properties = PropertyPlaceholderUtils.getProperties(location);

            for (String beanName : beanFactory.getBeanDefinitionNames()) {
                PropertyValues propertyValues = beanFactory.getBeanDefinition(beanName).getPropertyValues();
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if (!(value instanceof String)) {
                        continue;
                    }
                    value = PropertyPlaceholderUtils.resolvePlaceholder(properties, (String) value);
                    propertyValues.addPropertyValue(PropertyValue.of(propertyValue.getName(), value));
                }
            }

            beanFactory.addEmbeddedValueResolver(new PlaceholderResolvingStringValueResolver(properties));
        } catch (Exception e) {
            throw new BeansException("Could not load properties", e);
        }
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {
        private final Properties properties;

        private PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String value) {
            return PropertyPlaceholderUtils.resolvePlaceholder(properties, value);
        }
    }
}