package com.github.bourbon.common.bitmap;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 11:14
 */
public class BitMapTest {

    @Test
    public void test() {
        BitMap bitMap = BitMap.of();
        Assert.assertFalse(bitMap.contains(1));
        bitMap.add(1);
        Assert.assertTrue(bitMap.contains(1));
        bitMap.add(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Assert.assertEquals(bitMap.size(), 10);
        bitMap.remove(1);
        Assert.assertEquals(bitMap.size(), 9);
        bitMap.clear();
        Assert.assertTrue(bitMap.isEmpty());
    }
}