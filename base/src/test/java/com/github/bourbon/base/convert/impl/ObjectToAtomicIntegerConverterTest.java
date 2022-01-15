package com.github.bourbon.base.convert.impl;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 15:22
 */
public class ObjectToAtomicIntegerConverterTest {

    @Test
    public void test() {
        ObjectToNumberConverter converter = new ObjectToAtomicIntegerConverter();
        Assert.assertTrue(converter.convertInternal(1) instanceof AtomicInteger);
        Assert.assertEquals(converter.convertInternal(3).longValue(), 3L);
    }
}