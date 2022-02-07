package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.core.convert.TypeDescriptor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 16:11
 */
interface ConditionalGenericConverter extends org.springframework.core.convert.converter.ConditionalGenericConverter {

    default String[] getElements(String source, String delimiter) {
        return CharSequenceUtils.delimitedListToStringArray(source, BooleanUtils.defaultIfFalse(!Delimiter.NONE.equals(delimiter), delimiter, null));
    }

    default String getDelimiter(TypeDescriptor type) {
        return ObjectUtils.defaultIfNullElseFunction(type.getAnnotation(Delimiter.class), Delimiter::value, StringConstants.COMMA);
    }
}