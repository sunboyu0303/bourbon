package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 21:57
 */
public class StringToDoubleConverter implements StringConverter<Double> {

    @Override
    public Double convert(String str, Double defaultValue) {
        return BooleanUtils.defaultIfPredicate(str, s -> !CharSequenceUtils.isBlank(s), Double::parseDouble, defaultValue);
    }
}