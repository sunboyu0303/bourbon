package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 21:59
 */
public class StringToShortConverter implements StringConverter<Short> {

    @Override
    public Short convert(String str, Short defaultValue) {
        return BooleanUtils.defaultIfPredicate(str, s -> !CharSequenceUtils.isBlank(s), Short::parseShort, defaultValue);
    }
}