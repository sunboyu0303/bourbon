package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.NumberUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 10:13
 */
public class StringToNumberConverter<T extends Number> implements StringConverter<T> {

    private final Class<T> targetType;

    public StringToNumberConverter(Class<T> clazz) {
        targetType = clazz;
    }

    @Override
    public T convert(String str, T defaultValue) {
        return BooleanUtils.defaultIfPredicate(str, s -> !CharSequenceUtils.isBlank(s), s -> NumberUtils.parseNumber(s, targetType), defaultValue);
    }
}