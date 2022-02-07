package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 13:12
 */
final class CollectionToDelimitedStringConverter implements ConditionalGenericConverter {

    private final ConversionService conversionService;

    CollectionToDelimitedStringConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(Collection.class, String.class));
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        TypeDescriptor sourceElementType = sourceType.getElementTypeDescriptor();
        if (targetType == null || sourceElementType == null) {
            return true;
        }
        return conversionService.canConvert(sourceElementType, targetType) || sourceElementType.getType().isAssignableFrom(targetType.getType());
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return ObjectUtils.defaultIfNullElseFunction(source, s -> convert((Collection<?>) s, sourceType, targetType));
    }

    private Object convert(Collection<?> source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return BooleanUtils.defaultIfFalse(!source.isEmpty(),
                () -> source.stream().map(element -> convertElement(element, sourceType, targetType)).collect(Collectors.joining(getDelimiter(sourceType))),
                StringConstants.EMPTY
        );
    }

    private String convertElement(Object element, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return String.valueOf(conversionService.convert(element, sourceType.elementTypeDescriptor(element), targetType));
    }
}