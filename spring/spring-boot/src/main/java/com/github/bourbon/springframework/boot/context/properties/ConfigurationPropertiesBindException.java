package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.utils.ClassUtils;
import org.springframework.beans.factory.BeanCreationException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 18:17
 */
public class ConfigurationPropertiesBindException extends BeanCreationException {

    private static final long serialVersionUID = 2283835226129258880L;

    private final ConfigurationPropertiesBean bean;

    ConfigurationPropertiesBindException(ConfigurationPropertiesBean bean, Exception cause) {
        super(bean.getName(), getMessage(bean), cause);
        this.bean = bean;
    }

    public Class<?> getBeanType() {
        return bean.getType();
    }

    public ConfigurationProperties getAnnotation() {
        return bean.getAnnotation();
    }

    private static String getMessage(ConfigurationPropertiesBean bean) {
        ConfigurationProperties annotation = bean.getAnnotation();
        StringBuilder message = new StringBuilder();
        message.append("Could not bind properties to '");
        message.append(ClassUtils.getSimpleClassName(bean.getType())).append("' : ");
        message.append("prefix=").append(annotation.prefix());
        message.append(", ignoreInvalidFields=").append(annotation.ignoreInvalidFields());
        message.append(", ignoreUnknownFields=").append(annotation.ignoreUnknownFields());
        return message.toString();
    }
}