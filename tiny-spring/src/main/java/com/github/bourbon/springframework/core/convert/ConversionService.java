package com.github.bourbon.springframework.core.convert;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 10:02
 */
public interface ConversionService {

    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    <T> T convert(Object source, Class<T> targetType);
}