package com.github.bourbon.springframework.boot.context.properties.source;

import java.util.List;
import java.util.function.BiPredicate;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 18:45
 */
interface PropertyMapper {

    BiPredicate<ConfigurationPropertyName, ConfigurationPropertyName> DEFAULT_ANCESTOR_OF_CHECK = ConfigurationPropertyName::isAncestorOf;

    List<String> map(ConfigurationPropertyName configurationPropertyName);

    ConfigurationPropertyName map(String propertySourceName);

    default BiPredicate<ConfigurationPropertyName, ConfigurationPropertyName> getAncestorOfCheck() {
        return DEFAULT_ANCESTOR_OF_CHECK;
    }
}