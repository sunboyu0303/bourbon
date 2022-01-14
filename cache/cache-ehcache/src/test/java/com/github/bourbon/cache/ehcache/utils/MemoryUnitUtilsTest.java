package com.github.bourbon.cache.ehcache.utils;

import org.ehcache.config.units.MemoryUnit;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 11:04
 */
public class MemoryUnitUtilsTest {

    @Test
    public void test() {
        Assert.assertEquals(MemoryUnitUtils.getMemoryUnit("Kb"), MemoryUnit.MB);
        Assert.assertEquals(MemoryUnitUtils.getMemoryUnit("KB"), MemoryUnit.KB);
        Assert.assertEquals(MemoryUnitUtils.getMemoryUnit("Kb", MemoryUnit.KB), MemoryUnit.KB);
    }
}