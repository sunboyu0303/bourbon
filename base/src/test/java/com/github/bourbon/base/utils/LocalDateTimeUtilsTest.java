package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.DateConstants;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 11:48
 */
public class LocalDateTimeUtilsTest {

    @Test
    public void test() {
        LocalDate localDate = LocalDateTimeUtils.localDate(2022, 2, 22);
        Assert.assertEquals(localDate.toString(), LocalDateTimeUtils.localDateFormat(localDate, DateConstants.DEFAULT_LOCAL_DATE_FORMATTER));

        LocalTime localTime = LocalDateTimeUtils.localTime(22, 22, 22);
        Assert.assertEquals(localTime.toString(), LocalDateTimeUtils.localTimeFormat(localTime, DateConstants.DEFAULT_LOCAL_TIME_FORMATTER));

        LocalDateTime localDateTime = LocalDateTimeUtils.localDateTime(localDate, localTime);
        Assert.assertNotEquals(localDateTime.toString(), LocalDateTimeUtils.localDateTimeFormat(localDateTime, DateConstants.DEFAULT_LOCAL_DATE_TIME_FORMATTER));
    }
}