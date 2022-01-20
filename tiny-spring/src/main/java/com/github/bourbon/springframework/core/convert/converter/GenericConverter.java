package com.github.bourbon.springframework.core.convert.converter;

import com.github.bourbon.base.lang.Pair;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 10:06
 */
public interface GenericConverter {

    Set<Pair<Class<?>, Class<?>>> getConvertibleTypes();

    Object convert(Object source, Class<?> sourceType, Class<?> targetType);
}