package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.ObjectConverter;
import com.github.bourbon.base.utils.BooleanUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 14:27
 */
public class ObjectToAtomicBooleanConverter implements ObjectConverter<AtomicBoolean> {

    @Override
    public AtomicBoolean convertInternal(Object o) {
        return new AtomicBoolean(BooleanUtils.defaultSupplierIfAssignableFrom(
                o, Boolean.class, Boolean.class::cast, () -> BooleanUtils.toBoolean(ObjectConverter.convertToStr(o))
        ));
    }
}