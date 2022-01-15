package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 21:54
 */
public class StringToByteConverter implements StringConverter<Byte> {

    @Override
    public Byte convert(String str, Byte defaultValue) {
        return BooleanUtils.defaultIfPredicate(str, s -> !CharSequenceUtils.isBlank(s), Byte::parseByte, defaultValue);
    }
}