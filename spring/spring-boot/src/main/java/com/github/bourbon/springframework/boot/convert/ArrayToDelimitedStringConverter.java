package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.util.ObjectUtils;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 13:08
 */
final class ArrayToDelimitedStringConverter implements ConditionalGenericConverter {

    private final CollectionToDelimitedStringConverter delegate;

    ArrayToDelimitedStringConverter(ConversionService conversionService) {
        this.delegate = new CollectionToDelimitedStringConverter(conversionService);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(Object[].class, String.class));
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return delegate.matches(sourceType, targetType);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return delegate.convert(ListUtils.newArrayList(ObjectUtils.toObjectArray(source)), sourceType, targetType);
    }
}