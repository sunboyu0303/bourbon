package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.ObjectConverter;
import com.github.bourbon.base.lang.SystemClock;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 15:45
 */
public class ObjectToDateConverterTest {

    @Test
    public void test() {
        ObjectConverter<Date> converter = new ObjectToDateConverter();
        Date date = new Date();
        Assert.assertEquals(date.getTime(), converter.convertInternal(date).getTime());
        long time = SystemClock.currentTimeMillis();
        Assert.assertEquals(time, converter.convertInternal(time).getTime());
    }
}