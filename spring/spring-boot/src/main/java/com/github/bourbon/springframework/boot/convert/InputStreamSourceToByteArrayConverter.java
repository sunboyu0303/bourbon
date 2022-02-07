package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.origin.Origin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 12:29
 */
class InputStreamSourceToByteArrayConverter implements Converter<InputStreamSource, byte[]> {

    @Override
    public byte[] convert(InputStreamSource source) {
        try {
            return FileCopyUtils.copyToByteArray(source.getInputStream());
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read from " + getName(source), ex);
        }
    }

    private String getName(InputStreamSource source) {
        return ObjectUtils.defaultSupplierIfNullElseFunction(Origin.from(source), Object::toString, () -> BooleanUtils.defaultIfAssignableFrom(source, Resource.class, s -> ((Resource) s).getDescription(), "input stream source"));
    }
}