package com.github.bourbon.springframework.boot.convert;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 14:06
 */
final class CharArrayFormatter implements Formatter<char[]> {

    @Override
    public String print(char[] object, Locale locale) {
        return new String(object);
    }

    @Override
    public char[] parse(String text, Locale locale) throws ParseException {
        return text.toCharArray();
    }
}