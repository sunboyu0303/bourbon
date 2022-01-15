package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 21:45
 */
public class StringToBooleanConverter implements StringConverter<Boolean> {

    @Override
    public Boolean convert(String str, Boolean defaultValue) {
        return BooleanUtils.defaultIfPredicate(str, s -> !CharSequenceUtils.isBlank(s), Boolean::parseBoolean, defaultValue);
    }
}