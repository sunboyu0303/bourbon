package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 13:54
 */
final class DelimitedStringToCollectionConverter implements ConditionalGenericConverter {

    private final ConversionService conversionService;

    DelimitedStringToCollectionConverter(ConversionService conversionService) {
        this.conversionService = ObjectUtils.requireNonNull(conversionService, "ConversionService must not be null");
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(String.class, Collection.class));
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        TypeDescriptor typeDescriptor = targetType.getElementTypeDescriptor();
        return typeDescriptor == null || conversionService.canConvert(sourceType, typeDescriptor);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return ObjectUtils.defaultIfNull(source, s -> convert((String) s, sourceType, targetType));
    }

    private Object convert(String source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        String[] elements = getElements(source, getDelimiter(targetType));
        TypeDescriptor elementDescriptor = targetType.getElementTypeDescriptor();
        Collection<Object> target = createCollection(targetType, elementDescriptor, elements.length);
        Stream<Object> stream = Arrays.stream(elements).map(String::trim);
        if (elementDescriptor != null) {
            stream = stream.map(element -> conversionService.convert(element, sourceType, elementDescriptor));
        }
        stream.forEach(target::add);
        return target;
    }

    private Collection<Object> createCollection(TypeDescriptor targetType, TypeDescriptor elementDescriptor, int length) {
        return CollectionFactory.createCollection(targetType.getType(), ObjectUtils.defaultIfNull(elementDescriptor, TypeDescriptor::getType), length);
    }
}