package com.github.bourbon.base.convert;

import com.github.bourbon.base.utils.*;

import java.util.Collection;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 22:49
 */
public interface ObjectConverter<T> extends Converter<Object, T> {

    @SuppressWarnings("unchecked")
    @Override
    default T convert(Object value, T defaultValue) {
        Class<T> targetType = getTargetType();

        if (null == targetType && null == defaultValue) {
            throw new NullPointerException("[type] and [defaultValue] are both null for Converter [" + getClass().getName() + "], we can not know what type to convert !");
        }

        if (null == targetType) {
            targetType = (Class<T>) defaultValue.getClass();
        }

        if (null == value) {
            return defaultValue;
        }

        if (null != defaultValue && !targetType.isInstance(defaultValue)) {
            throw new IllegalArgumentException("Default value [" + defaultValue + "](" + defaultValue.getClass() + ") is not the instance of [" + targetType + "]");
        }

        if (targetType.isInstance(value) && !Map.class.isAssignableFrom(targetType)) {
            return targetType.cast(value);
        }

        return ObjectUtils.defaultIfNull(convertInternal(value), defaultValue);
    }

    @SuppressWarnings("unchecked")
    default Class<T> getTargetType() {
        return (Class<T>) ClassUtils.getTypeArgument(getClass());
    }

    T convertInternal(Object value);

    static String convertToStr(Object o) {
        if (ObjectUtils.isNull(o)) {
            return null;
        }
        if (o instanceof Collection) {
            return CollectionUtils.toString((Collection<?>) o);
        }
        if (ArrayUtils.isArray(o)) {
            return ArrayUtils.toString(o);
        }
        if (o instanceof CharSequence) {
            return o.toString();
        }
        return CharUtils.isChar(o) ? CharUtils.toString((Character) o) : o.toString();
    }
}