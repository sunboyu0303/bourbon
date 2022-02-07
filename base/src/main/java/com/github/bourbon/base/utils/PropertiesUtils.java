package com.github.bourbon.base.utils;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/18 16:14
 */
public final class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    public static Properties getProperties(ClassLoader cl, String... locations) {
        Properties properties = new Properties();
        Arrays.stream(locations).forEach(l -> properties.putAll(getProperties(cl, l)));
        return properties;
    }

    public static Properties getProperties(ClassLoader cl, String location) {
        Properties properties = new Properties();
        URL url = ObjectUtils.defaultSupplierIfNullElseFunction(cl, c -> c.getResource(location), () -> ClassLoader.getSystemResource(location));
        if (url != null) {
            try (InputStream is = url.openStream()) {
                properties.load(is);
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
        return properties;
    }

    public static Properties getAllProperties(ClassLoader cl, String... locations) {
        Properties properties = new Properties();
        Arrays.stream(locations).forEach(l -> properties.putAll(getAllProperties(cl, l)));
        return properties;
    }

    public static Properties getAllProperties(ClassLoader cl, String location) {
        Properties properties = new Properties();
        Enumeration<URL> urls = ObjectUtils.defaultSupplierIfNullElseFunction(cl,
                c -> {
                    try {
                        return c.getResources(location);
                    } catch (IOException e) {
                        return null;
                    }
                },
                () -> {
                    try {
                        return ClassLoader.getSystemResources(location);
                    } catch (IOException e) {
                        return null;
                    }
                }
        );
        if (urls != null) {
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                try (InputStream is = url.openStream()) {
                    Properties tmp = new Properties();
                    tmp.load(is);
                    properties.putAll(tmp);
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        }
        return properties;
    }

    private PropertiesUtils() {
    }
}