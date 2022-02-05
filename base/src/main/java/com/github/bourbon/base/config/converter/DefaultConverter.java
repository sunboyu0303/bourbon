package com.github.bourbon.base.config.converter;

import com.github.bourbon.base.convert.Convert;
import com.github.bourbon.base.convert.StringConverter;
import com.github.bourbon.base.utils.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 16:19
 */
public final class DefaultConverter implements StringConverter<Object> {

    private static final DefaultConverter INSTANCE = new DefaultConverter();

    public static DefaultConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Object convert(String value, Object defaultValue) {

        if (value == null) {
            return null;
        }

        if (defaultValue == null) {
            throw new NullPointerException("convert type cannot be null");
        }

        Class<?> type = defaultValue.getClass();
        if (CharSequenceUtils.isBlank(value) && type != String.class) {
            return null;
        }

        if (type == String.class) {
            return value;
        }

        if (BooleanUtils.isBoolean(type)) {
            return Convert.toBoolean(value, (Boolean) defaultValue);
        }

        if (CharUtils.isCharacter(type)) {
            return Convert.toCharacter(value, (Character) defaultValue);
        }

        if (FloatUtils.isFloat(type)) {
            return Convert.toFloat(value, (Float) defaultValue);
        }

        if (DoubleUtils.isDouble(type)) {
            return Convert.toDouble(value, (Double) defaultValue);
        }

        if (ByteUtils.isByte(type)) {
            return Convert.toByte(value, (Byte) defaultValue);
        }

        if (ShortUtils.isShort(type)) {
            return Convert.toShort(value, (Short) defaultValue);
        }

        if (IntUtils.isInteger(type)) {
            return Convert.toInteger(value, (Integer) defaultValue);
        }

        if (LongUtils.isLong(type)) {
            return Convert.toLong(value, (Long) defaultValue);
        }

        throw new IllegalArgumentException("DefaultConverter not support type [" + type + "], failed to convert value [" + value + "].");
    }

    private DefaultConverter() {
    }
}