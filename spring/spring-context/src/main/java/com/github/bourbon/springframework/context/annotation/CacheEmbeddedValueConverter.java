package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.base.convert.Convert;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/6 18:28
 */
public interface CacheEmbeddedValueConverter extends EmbeddedValueResolverAware {

    StringValueResolver getEmbeddedValueResolver();

    default Boolean getBoolean(String str) {
        return ObjectUtils.defaultIfNullElseFunction(str, s -> ObjectUtils.defaultIfNullElseFunction(getEmbeddedValue(s), Convert::toBoolean));
    }

    default Float getFloat(String str) {
        return ObjectUtils.defaultIfNullElseFunction(str, s -> ObjectUtils.defaultIfNullElseFunction(getEmbeddedValue(s), Convert::toFloat));
    }

    default Double getDouble(String str) {
        return ObjectUtils.defaultIfNullElseFunction(str, s -> ObjectUtils.defaultIfNullElseFunction(getEmbeddedValue(s), Convert::toDouble));
    }

    default Byte getByte(String str) {
        return ObjectUtils.defaultIfNullElseFunction(str, s -> ObjectUtils.defaultIfNullElseFunction(getEmbeddedValue(s), Convert::toByte));
    }

    default Short getShort(String str) {
        return ObjectUtils.defaultIfNullElseFunction(str, s -> ObjectUtils.defaultIfNullElseFunction(getEmbeddedValue(s), Convert::toShort));
    }

    default Integer getInteger(String str) {
        return ObjectUtils.defaultIfNullElseFunction(str, s -> ObjectUtils.defaultIfNullElseFunction(getEmbeddedValue(s), Convert::toInteger));
    }

    default Long getLong(String str) {
        return ObjectUtils.defaultIfNullElseFunction(str, s -> ObjectUtils.defaultIfNullElseFunction(getEmbeddedValue(s), Convert::toLong));
    }

    default String getEmbeddedValue(String str) {
        return ObjectUtils.defaultIfNullElseFunction(str, s -> getEmbeddedValueResolver().resolveStringValue(s));
    }
}