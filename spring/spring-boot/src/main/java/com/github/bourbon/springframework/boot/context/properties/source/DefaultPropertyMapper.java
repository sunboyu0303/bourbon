package com.github.bourbon.springframework.boot.context.properties.source;


import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 18:46
 */
final class DefaultPropertyMapper implements PropertyMapper {

    public static final PropertyMapper INSTANCE = new DefaultPropertyMapper();

    private LastMapping<ConfigurationPropertyName, List<String>> lastMappedConfigurationPropertyName;

    private LastMapping<String, ConfigurationPropertyName> lastMappedPropertyName;

    private DefaultPropertyMapper() {
    }

    @Override
    public List<String> map(ConfigurationPropertyName configurationPropertyName) {
        LastMapping<ConfigurationPropertyName, List<String>> last = lastMappedConfigurationPropertyName;
        if (last != null && last.isFrom(configurationPropertyName)) {
            return last.getMapping();
        }
        List<String> mapping = ListUtils.newArrayList(configurationPropertyName.toString());
        lastMappedConfigurationPropertyName = new LastMapping<>(configurationPropertyName, mapping);
        return mapping;
    }

    @Override
    public ConfigurationPropertyName map(String propertySourceName) {
        LastMapping<String, ConfigurationPropertyName> last = lastMappedPropertyName;
        if (last != null && last.isFrom(propertySourceName)) {
            return last.getMapping();
        }
        ConfigurationPropertyName mapping = tryMap(propertySourceName);
        lastMappedPropertyName = new LastMapping<>(propertySourceName, mapping);
        return mapping;
    }

    private ConfigurationPropertyName tryMap(String propertySourceName) {
        try {
            ConfigurationPropertyName convertedName = ConfigurationPropertyName.adapt(propertySourceName, CharConstants.DOT);
            if (!convertedName.isEmpty()) {
                return convertedName;
            }
        } catch (Exception ex) {
            // ignore
        }
        return ConfigurationPropertyName.EMPTY;
    }

    private static class LastMapping<T, M> {

        private final T from;

        private final M mapping;

        LastMapping(T from, M mapping) {
            this.from = from;
            this.mapping = mapping;
        }

        boolean isFrom(T from) {
            return ObjectUtils.nullSafeEquals(from, this.from);
        }

        M getMapping() {
            return mapping;
        }
    }
}