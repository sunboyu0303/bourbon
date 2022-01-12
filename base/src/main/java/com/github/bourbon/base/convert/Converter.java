package com.github.bourbon.base.convert;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 10:31
 */
@SPI(scope = ExtensionScope.FRAMEWORK)
public interface Converter<S, T> {

    T convert(S value, T defaultValue) throws IllegalArgumentException;

    default T convertQuietly(S s, T defaultValue) {
        try {
            return convert(s, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}