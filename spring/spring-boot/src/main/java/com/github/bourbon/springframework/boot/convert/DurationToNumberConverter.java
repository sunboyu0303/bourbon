package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ReflectionUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 11:35
 */
final class DurationToNumberConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(Duration.class, Number.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return ObjectUtils.defaultIfNullElseFunction(source, s -> convert((Duration) s, getDurationUnit(sourceType), targetType.getObjectType()));
    }

    private ChronoUnit getDurationUnit(TypeDescriptor sourceType) {
        return ObjectUtils.defaultIfNullElseFunction(sourceType.getAnnotation(DurationUnit.class), DurationUnit::value);
    }

    private Object convert(Duration source, ChronoUnit unit, Class<?> type) {
        try {
            return type.getConstructor(String.class).newInstance(String.valueOf(DurationStyle.Unit.fromChronoUnit(unit).longValue(source)));
        } catch (Exception ex) {
            ReflectionUtils.rethrowRuntimeException(ex);
            throw new IllegalStateException(ex);
        }
    }
}