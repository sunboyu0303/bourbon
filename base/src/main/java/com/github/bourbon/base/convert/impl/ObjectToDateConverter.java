package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.ObjectConverter;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Date;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 16:40
 */
public class ObjectToDateConverter implements ObjectConverter<Date> {

    @Override
    public Date convertInternal(Object object) {
        return ObjectUtils.defaultIfNull(object, o -> BooleanUtils.defaultSupplierIfAssignableFrom(
                o, Date.class, Date.class::cast, () -> BooleanUtils.defaultSupplierIfAssignableFrom(
                        o, Number.class, n -> new Date(((Number) n).longValue()),
                        () -> BooleanUtils.defaultIfAssignableFrom(o, CharSequence.class, s -> new Date(Long.parseLong(s.toString())))
                )
        ));
    }
}