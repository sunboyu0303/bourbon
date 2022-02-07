package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.origin.Origin;
import com.github.bourbon.springframework.boot.origin.OriginProvider;
import com.github.bourbon.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.style.ToStringCreator;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 16:01
 */
public final class ConfigurationProperty implements OriginProvider, Comparable<ConfigurationProperty> {

    private final ConfigurationPropertySource source;
    private final ConfigurationPropertyName name;
    private final Object value;
    private final Origin origin;

    public ConfigurationProperty(ConfigurationPropertyName name, Object value, Origin origin) {
        this(null, name, value, origin);
    }

    private ConfigurationProperty(ConfigurationPropertySource source, ConfigurationPropertyName name, Object value, Origin origin) {
        this.source = source;
        this.name = ObjectUtils.requireNonNull(name, "Name must not be null");
        this.value = ObjectUtils.requireNonNull(value, "Value must not be null");
        this.origin = origin;
    }

    public ConfigurationPropertySource getSource() {
        return source;
    }

    public ConfigurationPropertyName getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Origin getOrigin() {
        return origin;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ConfigurationProperty other = (ConfigurationProperty) obj;
        return ObjectUtils.nullSafeEquals(name, other.name) && ObjectUtils.nullSafeEquals(value, other.value);
    }

    @Override
    public int hashCode() {
        return 31 * ObjectUtils.nullSafeHashCode(name) + ObjectUtils.nullSafeHashCode(value);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this).append("name", name).append("value", value).append("origin", origin).toString();
    }

    @Override
    public int compareTo(ConfigurationProperty other) {
        return name.compareTo(other.name);
    }

    static ConfigurationProperty of(ConfigurationPropertyName name, OriginTrackedValue value) {
        return ObjectUtils.defaultIfNullElseFunction(value, v -> new ConfigurationProperty(name, v.getValue(), v.getOrigin()));
    }

    static ConfigurationProperty of(ConfigurationPropertySource source, ConfigurationPropertyName name, Object value, Origin origin) {
        return ObjectUtils.defaultIfNullElseFunction(value, v -> new ConfigurationProperty(source, name, value, origin));
    }
}