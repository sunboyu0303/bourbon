package com.github.bourbon.springframework.core;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.PropertiesUtils;
import com.github.bourbon.base.utils.SystemUtils;

import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/18 15:33
 */
public final class SpringProperties {

    private static final Properties localProperties = PropertiesUtils.getProperties(
            SpringProperties.class.getClassLoader(), "spring.properties"
    );

    public static void setProperty(String key, String value) {
        if (value != null) {
            localProperties.setProperty(key, value);
        } else {
            localProperties.remove(key);
        }
    }

    public static String getProperty(String key) {
        return ObjectUtils.defaultSupplierIfNull(localProperties.getProperty(key), () -> SystemUtils.get(key));
    }

    public static void setFlag(String key) {
        localProperties.put(key, Boolean.TRUE.toString());
    }

    public static boolean getFlag(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    private SpringProperties() {
    }
}