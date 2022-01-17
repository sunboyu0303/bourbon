package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.unit.DataSize;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 12:24
 */
final class NumberToDataSizeConverter implements GenericConverter {

    private final StringToDataSizeConverter delegate = new StringToDataSizeConverter();

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(Number.class, DataSize.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return delegate.convert(ObjectUtils.defaultIfNull(source, Object::toString), TypeDescriptor.valueOf(String.class), targetType);
    }
}