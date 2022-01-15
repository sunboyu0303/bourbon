package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.ObjectConverter;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 15:19
 */
public class ObjectToAtomicBooleanConverterTest {

    @Test
    public void test() {
        ObjectConverter<AtomicBoolean> converter = new ObjectToAtomicBooleanConverter();
        Assert.assertTrue(converter.convertInternal("true").get());
        Assert.assertFalse(converter.convertInternal("false").get());
        Assert.assertFalse(converter.convertInternal("1").get());
    }
}