package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.convert.ObjectConverter;
import com.github.bourbon.base.utils.BooleanUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 14:22
 */
public class ObjectToBooleanConverter implements ObjectConverter<Boolean> {

    @Override
    public Boolean convertInternal(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number) o).longValue() != 0L;
        }
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        if (o instanceof Character) {
            return (Character) o != CharConstants.DEFAULT;
        }
        if (o instanceof CharSequence) {
            String text = o.toString();
            if ("true".equalsIgnoreCase(text)) {
                return true;
            }
            if ("false".equalsIgnoreCase(text)) {
                return false;
            }
            return Long.parseLong(text) != 0L;
        }
        return BooleanUtils.toBoolean(ObjectConverter.convertToStr(o));
    }
}