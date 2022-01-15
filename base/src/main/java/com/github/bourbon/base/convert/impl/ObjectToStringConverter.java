package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.ObjectConverter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 15:22
 */
public class ObjectToStringConverter implements ObjectConverter<String> {

    @Override
    public String convertInternal(Object value) {
        return ObjectConverter.convertToStr(value);
    }
}