package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.TypeDescriptor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 00:27
 */
final class StringToDurationConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(String.class, Duration.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return BooleanUtils.defaultIfFalse(!ObjectUtils.isEmpty(source), () -> convert(source.toString(), getDurationStyle(targetType), getChronoUnit(targetType)));
    }

    private Duration convert(String source, DurationStyle style, ChronoUnit unit) {
        return ObjectUtils.defaultSupplierIfNull(style, () -> DurationStyle.detect(source)).parse(source, unit);
    }
}