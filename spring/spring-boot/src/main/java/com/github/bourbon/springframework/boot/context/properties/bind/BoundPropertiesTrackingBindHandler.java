package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationProperty;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;

import java.util.function.Consumer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 16:50
 */
public class BoundPropertiesTrackingBindHandler extends AbstractBindHandler {

    private final Consumer<ConfigurationProperty> consumer;

    public BoundPropertiesTrackingBindHandler(Consumer<ConfigurationProperty> consumer) {
        this.consumer = ObjectUtils.requireNonNull(consumer, "Consumer must not be null");
    }

    @Override
    public Object onSuccess(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) {
        ConfigurationProperty configurationProperty = context.getConfigurationProperty();
        if (configurationProperty != null && name.equals(configurationProperty.getName())) {
            consumer.accept(configurationProperty);
        }
        return super.onSuccess(name, target, context, result);
    }
}