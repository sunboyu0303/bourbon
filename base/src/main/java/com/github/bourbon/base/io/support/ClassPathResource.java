package com.github.bourbon.base.io.support;

import com.github.bourbon.base.io.Resource;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.ClassLoaderUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 11:42
 */
public class ClassPathResource implements Resource {

    private final String path;

    private final ClassLoader classLoader;

    ClassPathResource(String path) {
        this(path, null);
    }

    ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "path must not be null");
        this.path = path;
        this.classLoader = ObjectUtils.defaultSupplierIfNull(classLoader, (Supplier<ClassLoader>) ClassLoaderUtils::getClassLoader);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return ObjectUtils.requireNonNull(classLoader.getResourceAsStream(path), () -> new FileNotFoundException(path + " cannot be opened because it does not exist"));
    }
}