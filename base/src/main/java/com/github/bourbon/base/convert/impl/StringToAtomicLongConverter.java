package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 16:50
 */
public class StringToAtomicLongConverter implements StringConverter<AtomicLong> {

    @Override
    public AtomicLong convert(String str, AtomicLong defaultValue) {
        return BooleanUtils.defaultIfPredicate(str, s -> !CharSequenceUtils.isBlank(s), s -> new AtomicLong(Long.parseLong(s)), defaultValue);
    }
}