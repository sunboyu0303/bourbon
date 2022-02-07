package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.time.Period;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 12:17
 */
final class NumberToPeriodConverter implements GenericConverter {

    private final StringToPeriodConverter delegate = new StringToPeriodConverter();

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(Number.class, Period.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return delegate.convert(ObjectUtils.defaultIfNullElseFunction(source, Object::toString), TypeDescriptor.valueOf(String.class), targetType);
    }
}