package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationProperty;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import com.github.bourbon.springframework.boot.origin.Origin;
import com.github.bourbon.springframework.boot.origin.OriginProvider;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/24 16:37
 */
public class BindException extends RuntimeException implements OriginProvider {

    private static final long serialVersionUID = 6264259321456667512L;

    private final transient Bindable<?> target;

    private final transient ConfigurationProperty property;

    private final transient ConfigurationPropertyName name;

    BindException(ConfigurationPropertyName name, Bindable<?> target, ConfigurationProperty property, Throwable cause) {
        super(buildMessage(name, target), cause);
        this.name = name;
        this.target = target;
        this.property = property;
    }

    public ConfigurationPropertyName getName() {
        return name;
    }

    public Bindable<?> getTarget() {
        return target;
    }

    public ConfigurationProperty getProperty() {
        return property;
    }

    @Override
    public Origin getOrigin() {
        return Origin.from(name);
    }

    private static String buildMessage(ConfigurationPropertyName name, Bindable<?> target) {
        StringBuilder message = new StringBuilder();
        message.append("Failed to bind properties");
        message.append(ObjectUtils.defaultIfNull(name, n -> " under '" + n + StringConstants.SINGLE_QUOTE, StringConstants.EMPTY));
        message.append(" to ").append(target.getType());
        return message.toString();
    }
}