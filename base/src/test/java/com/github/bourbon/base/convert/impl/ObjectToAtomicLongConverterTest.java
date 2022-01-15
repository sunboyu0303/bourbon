package com.github.bourbon.base.convert.impl;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 15:25
 */
public class ObjectToAtomicLongConverterTest {

    @Test
    public void test() {
        ObjectToNumberConverter converter = new ObjectToAtomicLongConverter();
        Assert.assertTrue(converter.convertInternal(1) instanceof AtomicLong);
    }
}