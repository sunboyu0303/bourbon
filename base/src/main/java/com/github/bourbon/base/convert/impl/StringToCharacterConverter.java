package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import com.github.bourbon.base.utils.CharSequenceUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 21:55
 */
public class StringToCharacterConverter implements StringConverter<Character> {

    @Override
    public Character convert(String source, Character defaultValue) {
        int length = CharSequenceUtils.length(source);
        if (length == 0) {
            return defaultValue;
        }
        if (length > 1) {
            throw new IllegalArgumentException("The source String is more than one character!");
        }
        return source.charAt(0);
    }
}