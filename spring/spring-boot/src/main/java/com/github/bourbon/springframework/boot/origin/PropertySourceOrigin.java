package com.github.bourbon.springframework.boot.origin;

import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.core.env.PropertySource;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 18:59
 */
public class PropertySourceOrigin implements Origin {

    private final PropertySource<?> propertySource;

    private final String propertyName;

    public PropertySourceOrigin(PropertySource<?> propertySource, String propertyName) {
        this.propertySource = ObjectUtils.requireNonNull(propertySource, "PropertySource must not be null");
        this.propertyName = ObjectUtils.requireNonNull(propertyName, "PropertyName must not be empty");
    }

    public PropertySource<?> getPropertySource() {
        return propertySource;
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String toString() {
        return "\"" + propertyName + "\" from property source \"" + propertySource.getName() + "\"";
    }

    public static Origin get(PropertySource<?> propertySource, String name) {
        return ObjectUtils.defaultSupplierIfNull(OriginLookup.getOrigin(propertySource, name), () -> new PropertySourceOrigin(propertySource, name));
    }
}