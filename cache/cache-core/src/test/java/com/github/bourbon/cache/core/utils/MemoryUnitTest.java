package com.github.bourbon.cache.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/11 17:49
 */
public class MemoryUnitTest {

    @Test
    public void test() {
        Assert.assertNull(MemoryUnit.getMemoryUnit("mb"));
        Assert.assertEquals(MemoryUnit.getMemoryUnit("MB"), MemoryUnit.MB);
    }
}