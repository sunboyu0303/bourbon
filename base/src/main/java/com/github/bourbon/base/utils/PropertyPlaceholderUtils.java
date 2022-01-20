package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.io.Resource;
import com.github.bourbon.base.io.ResourceLoader;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/8 10:35
 */
public final class PropertyPlaceholderUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyPlaceholderUtils.class);
    private static final ResourceLoader RESOURCE_LOADER = ScopeModelUtils.getExtensionLoader(ResourceLoader.class).getDefaultExtension();
    private static final Map<String, Properties> PROPERTIES_MAP = new ConcurrentHashMap<>();
    private static final Properties EMPTY = new Properties();
    private static final String DEFAULT_PLACEHOLDER_PREFIX = StringConstants.DOLLAR + StringConstants.LEFT_BRACES;
    private static final String DEFAULT_PLACEHOLDER_SUFFIX = StringConstants.RIGHT_BRACES;

    public static Properties getAllProperties() {
        Properties properties = new Properties();
        PROPERTIES_MAP.values().forEach(properties::putAll);
        return properties;
    }

    public static Properties getProperties(String location) {
        return BooleanUtils.defaultIfPredicate(PROPERTIES_MAP.computeIfAbsent(location, o -> {
            try {
                Resource resource = RESOURCE_LOADER.getResource(location);
                Properties properties = new Properties();
                properties.load(resource.getInputStream());
                return properties;
            } catch (IOException e) {
                LOGGER.error(e);
                return EMPTY;
            }
        }), t -> t != EMPTY, t -> t);
    }

    public static String resolvePlaceholder(String location, String value) {
        return ObjectUtils.defaultIfNull(getProperties(location), p -> resolvePlaceholder(p, value));
    }

    public static String resolvePlaceholder(Properties properties, String value) {
        return ObjectUtils.defaultIfNull(properties, p -> {
            StringBuilder buffer = new StringBuilder(value);
            int startIdx = value.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
            int stopIdx = value.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
            if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
                buffer.replace(startIdx, stopIdx + 1, p.getProperty(value.substring(startIdx + 2, stopIdx)));
            }
            return buffer.toString();
        });
    }

    private PropertyPlaceholderUtils() {
    }
}