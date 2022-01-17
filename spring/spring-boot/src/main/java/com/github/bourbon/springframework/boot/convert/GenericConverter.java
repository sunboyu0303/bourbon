package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.unit.DataUnit;

import java.time.temporal.ChronoUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 16:31
 */
interface GenericConverter extends org.springframework.core.convert.converter.GenericConverter {

    default DurationStyle getDurationStyle(TypeDescriptor type) {
        return ObjectUtils.defaultIfNull(type.getAnnotation(DurationFormat.class), DurationFormat::value);
    }

    default ChronoUnit getChronoUnit(TypeDescriptor type) {
        return ObjectUtils.defaultIfNull(type.getAnnotation(DurationUnit.class), DurationUnit::value);
    }

    default DataUnit getDataUnit(TypeDescriptor type) {
        return ObjectUtils.defaultIfNull(type.getAnnotation(DataSizeUnit.class), DataSizeUnit::value);
    }
}