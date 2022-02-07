package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 12:09
 */
final class PeriodToStringConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new ConvertiblePair(Period.class, String.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return BooleanUtils.defaultIfFalse(!ObjectUtils.isEmpty(source), () -> convert((Period) source, getPeriodStyle(sourceType), getPeriodUnit(sourceType)));
    }

    private PeriodStyle getPeriodStyle(TypeDescriptor sourceType) {
        return ObjectUtils.defaultIfNullElseFunction(sourceType.getAnnotation(PeriodFormat.class), PeriodFormat::value);
    }

    private String convert(Period source, PeriodStyle style, ChronoUnit unit) {
        return ObjectUtils.defaultIfNull(style, PeriodStyle.ISO8601).print(source, unit);
    }

    private ChronoUnit getPeriodUnit(TypeDescriptor sourceType) {
        return ObjectUtils.defaultIfNullElseFunction(sourceType.getAnnotation(PeriodUnit.class), PeriodUnit::value);
    }
}