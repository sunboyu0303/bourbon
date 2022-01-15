package com.github.bourbon.base.convert.impl;

import com.github.bourbon.base.convert.StringConverter;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 15:59
 */
public class StringToAtomicIntegerConverterTest {

    @Test
    public void test() {
        StringConverter<AtomicInteger> converter = new StringToAtomicIntegerConverter();
        Assert.assertNotNull(converter.convert(null, new AtomicInteger()));
        Assert.assertNotNull(converter.convert("", new AtomicInteger()));
        Assert.assertNotNull(converter.convert("  ", new AtomicInteger()));
    }
}