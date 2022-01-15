package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 22:03
 */
public class StringToCharArrayConverter implements StringConverter<char[]> {

    @Override
    public char[] convert(String str, char[] defaultValue) {
        return BooleanUtils.defaultIfPredicate(str, s -> !CharSequenceUtils.isEmpty(s), String::toCharArray, defaultValue);
    }
}