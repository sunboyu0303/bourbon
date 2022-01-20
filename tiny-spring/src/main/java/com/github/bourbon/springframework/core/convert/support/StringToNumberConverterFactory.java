package com.github.bourbon.springframework.core.convert.support;

import com.github.bourbon.base.convert.Converter;
import com.github.bourbon.base.convert.impl.StringToNumberConverter;
import com.github.bourbon.springframework.core.convert.converter.ConverterFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 10:10
 */
public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {

    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumberConverter<>(targetType);
    }
}