package com.github.bourbon.base.lang.mutable;

import org.junit.Assert;
import org.junit.Test;

public class MutableBooleanTest {

    @Test
    public void test() {
        MutableBoolean mutableBoolean = new MutableBoolean();
        Assert.assertFalse(mutableBoolean.get());
        Assert.assertTrue(mutableBoolean.set(true).get());
    }
}