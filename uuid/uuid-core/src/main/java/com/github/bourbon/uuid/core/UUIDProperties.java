package com.github.bourbon.uuid.core;

import com.github.bourbon.base.convert.Convert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.PropertiesUtils;
import com.github.bourbon.uuid.core.constant.UUIDConstants;

import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/1 15:37
 */
public final class UUIDProperties {

    private static final Properties properties = PropertiesUtils.getProperties(UUIDProperties.class.getClassLoader(), UUIDConstants.UUID_PROPERTIES_PATH);

    public static Properties getProperties() {
        return properties;
    }

    public static int getInteger(String key, int defaultValue) {
        return Convert.toInteger(get(key), defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        return Convert.toLong(get(key), defaultValue);
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static String get(String key, String defaultValue) {
        return BooleanUtils.defaultIfPredicate(properties.getProperty(key), v -> !CharSequenceUtils.isBlank(v), String::trim, defaultValue);
    }
}