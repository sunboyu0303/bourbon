package com.github.bourbon.springframework.context.converter;

import com.github.bourbon.base.convert.Converter;
import com.github.bourbon.base.utils.CharSequenceUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 15:31
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter DATE_TIME_FORMATTER;

    public StringToLocalDateConverter(String pattern) {
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public LocalDate convert(String value, LocalDate defaultValue) throws IllegalArgumentException {
        return CharSequenceUtils.isEmpty(value) ? defaultValue : LocalDate.parse(value, DATE_TIME_FORMATTER);
    }
}