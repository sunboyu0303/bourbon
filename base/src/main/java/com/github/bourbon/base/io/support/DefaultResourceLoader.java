package com.github.bourbon.base.io.support;

import com.github.bourbon.base.io.Resource;
import com.github.bourbon.base.io.ResourceLoader;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 11:38
 */
public class DefaultResourceLoader implements ResourceLoader {

    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "location must not be null");
        return BooleanUtils.defaultSupplierIfFalse(location.startsWith(CLASSPATH_URL_PREFIX),
                () -> new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length())),
                () -> {
                    try {
                        return new UrlResource(new URL(location));
                    } catch (MalformedURLException e) {
                        return new FileSystemResource(location);
                    }
                }
        );
    }
}