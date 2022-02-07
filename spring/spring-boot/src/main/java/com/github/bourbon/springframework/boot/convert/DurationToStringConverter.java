package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.TypeDescriptor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 11:29
 */
final class DurationToStringConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(Duration.class, String.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return ObjectUtils.defaultIfNullElseFunction(source, s -> convert((Duration) s, getDurationStyle(sourceType), getChronoUnit(sourceType)));
    }

    private String convert(Duration source, DurationStyle style, ChronoUnit unit) {
        return ObjectUtils.defaultIfNull(style, DurationStyle.ISO8601).print(source, unit);
    }
}