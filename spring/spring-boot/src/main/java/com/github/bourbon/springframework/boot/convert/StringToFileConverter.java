package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.BooleanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 12:25
 */
class StringToFileConverter implements Converter<String, File> {

    private static final ResourceLoader resourceLoader = new DefaultResourceLoader(null);

    @Override
    public File convert(String source) {
        return BooleanUtils.defaultSupplierIfFalse(!ResourceUtils.isUrl(source), () -> {
            File file = new File(source);
            return BooleanUtils.defaultIfFalse(!file.exists(), () -> {
                Resource resource = resourceLoader.getResource(source);
                return BooleanUtils.defaultIfFalse(resource.exists(), () -> getFile(resource), file);
            }, file);
        }, () -> getFile(resourceLoader.getResource(source)));
    }

    private File getFile(Resource resource) {
        try {
            return resource.getFile();
        } catch (IOException ex) {
            throw new IllegalStateException("Could not retrieve file for " + resource + ": " + ex.getMessage());
        }
    }
}