package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 16:59
 */
public class StringToIntegerConverterTest {

    @Test
    public void test() {
        StringConverter<Integer> converter = new StringToIntegerConverter();
        Integer i = 127;
        Integer j = 127;
        Assert.assertTrue(converter.convert("", i) == j);
        i = 128;
        j = 128;
        Assert.assertFalse(converter.convert("", i) == j);
    }
}