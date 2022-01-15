package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.ObjectConverter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 15:25
 */
public class ObjectToCharacterConverter implements ObjectConverter<Character> {

    @Override
    public Character convertInternal(Object o) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(o, Boolean.class, v -> BooleanUtils.toChar((Boolean) v),
                () -> BooleanUtils.defaultIfPredicate(ObjectConverter.convertToStr(o), s -> !CharSequenceUtils.isBlank(s), s -> s.charAt(0))
        );
    }
}