package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 16:47
 */
public class StringToAtomicIntegerConverter implements StringConverter<AtomicInteger> {

    @Override
    public AtomicInteger convert(String str, AtomicInteger defaultValue) {
        return BooleanUtils.defaultIfPredicate(str, s -> !CharSequenceUtils.isBlank(s), s -> new AtomicInteger(Integer.parseInt(s)), defaultValue);
    }
}