package com.github.bourbon.springframework.core.io;

import com.github.bourbon.base.constant.StringConstants;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/27 23:12
 */
public class XPathProtocolResolver implements ProtocolResolver {

    /**
     * 查找OUTSIDE的配置路径，如果找不到，则返回null
     */
    private static final String X_PATH_OUTSIDE_PREFIX = StringConstants.COLONS;

    /**
     * 查找OUTSIDE 和inside，其中inside将会转换为CLASS_PATH
     */
    private static final String X_PATH_GLOBAL_PREFIX = StringConstants.STAR_COLON;

    private String customConfigPath;

    public XPathProtocolResolver(String configPath) {
        this.customConfigPath = configPath;
    }

    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        if (!location.startsWith(X_PATH_OUTSIDE_PREFIX) && !location.startsWith(X_PATH_GLOBAL_PREFIX)) {
            return null;
        }
        String real = path(location);
        Collection<String> fileLocations = searchLocationsForFile(real);
        for (String path : fileLocations) {
            Resource resource = resourceLoader.getResource(path);
            if (resource != null && resource.exists()) {
                return resource;
            }
        }
        boolean global = location.startsWith(X_PATH_GLOBAL_PREFIX);
        if (!global) {
            return null;
        }
        Collection<String> classpathLocations = searchLocationsForClasspath(real);
        for (String path : classpathLocations) {
            Resource resource = resourceLoader.getResource(path);
            if (resource != null && resource.exists()) {
                return resource;
            }
        }
        return resourceLoader.getResource(real);
    }

    private String path(String location) {
        return location.substring(2);
    }

    private Collection<String> searchLocationsForFile(String location) {
        Collection<String> locations = new LinkedHashSet<>();
        String _location = shaping(location);
        if (customConfigPath != null) {
            String prefix = ResourceUtils.FILE_URL_PREFIX + customConfigPath;
            if (!customConfigPath.endsWith(StringConstants.SLASH)) {
                locations.add(prefix + StringConstants.SLASH + _location);
            } else {
                locations.add(prefix + _location);
            }
        } else {
            locations.add(ResourceUtils.FILE_URL_PREFIX + "./config/" + _location);
        }
        locations.add(ResourceUtils.FILE_URL_PREFIX + StringConstants.DOT_SLASH + _location);
        return locations;
    }

    private Collection<String> searchLocationsForClasspath(String location) {
        Collection<String> locations = new LinkedHashSet<>();
        String _location = shaping(location);
        if (customConfigPath != null) {
            String prefix = ResourceUtils.CLASSPATH_URL_PREFIX + customConfigPath;
            if (!customConfigPath.endsWith(StringConstants.SLASH)) {
                locations.add(prefix + StringConstants.SLASH + _location);
            } else {
                locations.add(prefix + _location);
            }
        } else {
            locations.add(ResourceUtils.CLASSPATH_URL_PREFIX + "/config/" + _location);
        }
        locations.add(ResourceUtils.CLASSPATH_URL_PREFIX + StringConstants.SLASH + _location);
        return locations;
    }

    private String shaping(String location) {
        if (location.startsWith(StringConstants.DOT_SLASH)) {
            return location.substring(2);
        }
        if (location.startsWith(StringConstants.SLASH)) {
            return location.substring(1);
        }
        return location;
    }
}