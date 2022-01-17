package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 13:03
 */
class CharSequenceToObjectConverter implements ConditionalGenericConverter {

    private static final TypeDescriptor STRING = TypeDescriptor.valueOf(String.class);

    private static final TypeDescriptor BYTE_ARRAY = TypeDescriptor.valueOf(byte[].class);

    private static final Set<ConvertiblePair> TYPES = SetUtils.newHashSet(new ConvertiblePair(CharSequence.class, Object.class));

    private final ThreadLocal<Boolean> disable = new ThreadLocal<>();

    private final ConversionService conversionService;

    CharSequenceToObjectConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return TYPES;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (sourceType.getType() == String.class || disable.get() == Boolean.TRUE) {
            return false;
        }
        disable.set(Boolean.TRUE);
        try {
            if (conversionService.canConvert(sourceType, targetType) && !isStringConversionBetter(sourceType, targetType)) {
                return false;
            }
            return conversionService.canConvert(STRING, targetType);
        } finally {
            disable.remove();
        }
    }

    private boolean isStringConversionBetter(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (conversionService instanceof ApplicationConversionService) {
            ApplicationConversionService applicationConversionService = (ApplicationConversionService) conversionService;
            if (applicationConversionService.isConvertViaObjectSourceType(sourceType, targetType)) {
                return true;
            }
        }
        return (targetType.isArray() || targetType.isCollection()) && !targetType.equals(BYTE_ARRAY);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return conversionService.convert(source.toString(), STRING, targetType);
    }
}