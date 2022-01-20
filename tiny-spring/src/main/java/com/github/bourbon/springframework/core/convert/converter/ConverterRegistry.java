package com.github.bourbon.springframework.core.convert.converter;

import com.github.bourbon.base.convert.Converter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 10:04
 */
public interface ConverterRegistry {

    void addConverter(Converter<?, ?> converter);

    void addConverter(GenericConverter converter);

    void addConverterFactory(ConverterFactory<?, ?> converterFactory);
}