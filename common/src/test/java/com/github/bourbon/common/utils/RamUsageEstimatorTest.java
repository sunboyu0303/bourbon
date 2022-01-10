package com.github.bourbon.common.utils;

import com.carrotsearch.sizeof.RamUsageEstimator;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/11 16:24
 */
public class RamUsageEstimatorTest {

    @Test
    public void test() {
        Assert.assertEquals(RamUsageEstimator.sizeOf(new Object()), 16);
    }
}