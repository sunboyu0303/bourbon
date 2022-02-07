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
 * @date 2021/12/26 11:40
 */
final class StringToPeriodConverter implements GenericConverter {

    @Override
    public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
        return SetUtils.newHashSet(new GenericConverter.ConvertiblePair(String.class, Period.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return BooleanUtils.defaultIfFalse(!ObjectUtils.isEmpty(source), () -> convert(source.toString(), getStyle(targetType), getPeriodUnit(targetType)));
    }

    private PeriodStyle getStyle(TypeDescriptor targetType) {
        return ObjectUtils.defaultIfNullElseFunction(targetType.getAnnotation(PeriodFormat.class), PeriodFormat::value);
    }

    private ChronoUnit getPeriodUnit(TypeDescriptor targetType) {
        return ObjectUtils.defaultIfNullElseFunction(targetType.getAnnotation(PeriodUnit.class), PeriodUnit::value);
    }

    private Period convert(String source, PeriodStyle style, ChronoUnit unit) {
        return ObjectUtils.defaultSupplierIfNull(style, () -> PeriodStyle.detect(source)).parse(source, unit);
    }
}