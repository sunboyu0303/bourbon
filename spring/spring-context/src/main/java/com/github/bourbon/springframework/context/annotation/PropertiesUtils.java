package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/16 17:20
 */
public final class PropertiesUtils {

    private static final Properties PROP = new Properties();

    static {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

            for (Resource resource : resolver.getResources("classpath:**/*.properties")) {
                PropertiesLoaderUtils.fillProperties(PROP, resource);
            }

            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(resolver.getResources("classpath:**/*.yml"));
            if (ObjectUtils.nonNull(yaml.getObject())) {
                PROP.putAll(yaml.getObject());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("加载配置文件异常!");
        }
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        return ObjectUtils.defaultIfNull(PROP.get(key), Object::toString, defaultValue);
    }

    public static Boolean getBoolean(String key) {
        return Boolean.valueOf(getProperty(key));
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return ObjectUtils.defaultIfNull(getProperty(key), Boolean::valueOf, defaultValue);
    }

    public static Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public static Integer getInteger(String key, Integer defaultValue) {
        return ObjectUtils.defaultIfNull(getProperty(key), Integer::parseInt, defaultValue);
    }

    public static Long getLong(String key) {
        return getLong(key, null);
    }

    public static Long getLong(String key, Long defaultValue) {
        return ObjectUtils.defaultIfNull(getProperty(key), Long::parseLong, defaultValue);
    }

    public static Double getDouble(String key) {
        return getDouble(key, null);
    }

    public static Double getDouble(String key, Double defaultValue) {
        return ObjectUtils.defaultIfNull(getProperty(key), Double::parseDouble, defaultValue);
    }

    public static Properties getProperties() {
        return PROP;
    }

    private PropertiesUtils() {
    }
}