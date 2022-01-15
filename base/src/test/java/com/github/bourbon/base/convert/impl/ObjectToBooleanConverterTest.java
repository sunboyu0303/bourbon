package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.ObjectConverter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 15:38
 */
public class ObjectToBooleanConverterTest {

    @Test
    public void test() {
        ObjectConverter<Boolean> converter = new ObjectToBooleanConverter();
        Assert.assertNull(converter.convertInternal(null));
        Assert.assertTrue(converter.convertInternal(1L));
        Assert.assertFalse(converter.convertInternal(false));
        Assert.assertFalse(converter.convertInternal((char) 0));
        Assert.assertTrue(converter.convertInternal("true"));
        Assert.assertTrue(converter.convertInternal("1"));
    }
}