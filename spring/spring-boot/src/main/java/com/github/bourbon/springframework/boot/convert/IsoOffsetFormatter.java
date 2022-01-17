package com.github.bourbon.springframework.boot.convert;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 14:08
 */
class IsoOffsetFormatter implements Formatter<OffsetDateTime> {

    @Override
    public String print(OffsetDateTime object, Locale locale) {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(object);
    }

    @Override
    public OffsetDateTime parse(String text, Locale locale) throws ParseException {
        return OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}