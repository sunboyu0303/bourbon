package com.github.bourbon.springframework.boot.autoconfigure;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/17 17:54
 */
final class AutoConfigurationMetadataLoader {

    private static final String PATH = "META-INF/bourbon/spring-autoconfigure-metadata.properties";

    static AutoConfigurationMetadata loadMetadata(ClassLoader classLoader) {
        return loadMetadata(classLoader, PATH);
    }

    static AutoConfigurationMetadata loadMetadata(ClassLoader classLoader, String path) {
        try {
            Enumeration<URL> urls = classLoader != null ? classLoader.getResources(path) : ClassLoader.getSystemResources(path);
            Properties properties = new Properties();
            while (urls.hasMoreElements()) {
                properties.putAll(PropertiesLoaderUtils.loadProperties(new UrlResource(urls.nextElement())));
            }
            return loadMetadata(properties);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load @ConditionalOnClass location [" + path + "]", ex);
        }
    }

    static AutoConfigurationMetadata loadMetadata(Properties properties) {
        return new PropertiesAutoConfigurationMetadata(properties);
    }

    private static class PropertiesAutoConfigurationMetadata implements AutoConfigurationMetadata {

        private final Properties properties;

        private PropertiesAutoConfigurationMetadata(Properties properties) {
            this.properties = properties;
        }

        @Override
        public boolean wasProcessed(String className) {
            return properties.containsKey(className);
        }

        @Override
        public Integer getInteger(String className, String key) {
            return getInteger(className, key, null);
        }

        @Override
        public Integer getInteger(String className, String key, Integer defaultValue) {
            return ObjectUtils.defaultIfNull(get(className, key), Integer::valueOf, defaultValue);
        }

        @Override
        public Set<String> getSet(String className, String key) {
            return getSet(className, key, null);
        }

        @Override
        public Set<String> getSet(String className, String key, Set<String> defaultValue) {
            return ObjectUtils.defaultIfNull(get(className, key), t -> SetUtils.newHashSet(CharSequenceUtils.commaDelimitedListToStringArray(t)), defaultValue);
        }

        @Override
        public String get(String className, String key) {
            return get(className, key, null);
        }

        @Override
        public String get(String className, String key, String defaultValue) {
            return ObjectUtils.defaultIfNull(properties.getProperty(className + StringConstants.DOT + key), defaultValue);
        }
    }

    private AutoConfigurationMetadataLoader() {
    }
}