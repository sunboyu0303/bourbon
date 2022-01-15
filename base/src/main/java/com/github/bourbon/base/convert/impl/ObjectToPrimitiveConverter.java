package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.constant.*;
import com.github.bourbon.base.convert.Convert;
import com.github.bourbon.base.convert.ObjectConverter;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.function.Function;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 15:33
 */
public class ObjectToPrimitiveConverter implements ObjectConverter<Object> {

    private final Class<?> targetType;

    public ObjectToPrimitiveConverter(Class<?> clazz) {
        if (null == clazz) {
            throw new NullPointerException("PrimitiveConverter not allow null target type!");
        }
        if (!clazz.isPrimitive()) {
            throw new IllegalArgumentException("[" + clazz + "] is not a primitive class!");
        }
        targetType = clazz;
    }

    @Override
    public Object convertInternal(Object value) {
        return convert(value, targetType, this::convertToStr);
    }

    private String convertToStr(Object value) {
        return CharSequenceUtils.trim(ObjectConverter.convertToStr(value));
    }

    static Object convert(Object o, Class<?> clazz, Function<Object, String> f) {
        if (ByteConstants.TYPE == clazz) {
            return ObjectUtils.defaultIfNull(ObjectToNumberConverter.convert(o, Byte.class, f), ByteConstants.DEFAULT);
        }
        if (ShortConstants.TYPE == clazz) {
            return ObjectUtils.defaultIfNull(ObjectToNumberConverter.convert(o, Short.class, f), ShortConstants.DEFAULT);
        }
        if (IntConstants.TYPE == clazz) {
            return ObjectUtils.defaultIfNull(ObjectToNumberConverter.convert(o, Integer.class, f), IntConstants.DEFAULT);
        }
        if (LongConstants.TYPE == clazz) {
            return ObjectUtils.defaultIfNull(ObjectToNumberConverter.convert(o, Long.class, f), LongConstants.DEFAULT);
        }
        if (FloatConstants.TYPE == clazz) {
            return ObjectUtils.defaultIfNull(ObjectToNumberConverter.convert(o, Float.class, f), FloatConstants.DEFAULT);
        }
        if (DoubleConstants.TYPE == clazz) {
            return ObjectUtils.defaultIfNull(ObjectToNumberConverter.convert(o, Double.class, f), DoubleConstants.DEFAULT);
        }
        if (CharConstants.TYPE == clazz) {
            return Convert.toCharacter(o, CharConstants.DEFAULT);
        }
        if (BooleanConstants.TYPE == clazz) {
            return Convert.toBoolean(o, BooleanConstants.DEFAULT);
        }
        throw new UnsupportedOperationException("Unsupported target type: " + clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Object> getTargetType() {
        return (Class<Object>) targetType;
    }
}