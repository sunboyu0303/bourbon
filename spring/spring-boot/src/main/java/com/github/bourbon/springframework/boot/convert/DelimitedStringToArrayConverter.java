package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.lang.reflect.Array;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 13:46
 */
final class DelimitedStringToArrayConverter implements ConditionalGenericConverter {

    private final ConversionService conversionService;

    DelimitedStringToArrayConverter(ConversionService conversionService) {
        this.conversionService = ObjectUtils.requireNonNull(conversionService, "ConversionService must not be null");
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(String.class, Object[].class));
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        TypeDescriptor typeDescriptor = targetType.getElementTypeDescriptor();
        return typeDescriptor == null || conversionService.canConvert(sourceType, typeDescriptor);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return ObjectUtils.defaultIfNullElseFunction(source, s -> convert((String) s, sourceType, targetType));
    }

    private Object convert(String source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        String[] elements = getElements(source, getDelimiter(targetType));
        int len = elements.length;
        TypeDescriptor elementDescriptor = targetType.getElementTypeDescriptor();
        Object target = Array.newInstance(elementDescriptor.getType(), len);
        for (int i = 0; i < len; i++) {
            Array.set(target, i, conversionService.convert(elements[i].trim(), sourceType, elementDescriptor));
        }
        return target;
    }
}