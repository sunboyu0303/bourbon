package com.github.bourbon.springframework.core.convert.converter;

import com.github.bourbon.base.convert.Converter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 10:07
 */
public interface ConverterFactory<S, R> {

    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}