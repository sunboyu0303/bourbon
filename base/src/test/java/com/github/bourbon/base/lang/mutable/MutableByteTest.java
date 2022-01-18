package com.github.bourbon.base.lang.mutable;

import org.junit.Assert;
import org.junit.Test;

public class MutableByteTest {

    @Test
    public void test() {
        MutableByte mutableByte = new MutableByte();
        Assert.assertNotNull(mutableByte.get());
        Assert.assertTrue(mutableByte.increment().byteValue() > 0);
    }
}