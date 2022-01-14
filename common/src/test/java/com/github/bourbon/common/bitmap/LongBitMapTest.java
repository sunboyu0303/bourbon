package com.github.bourbon.common.bitmap;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 11:18
 */
public class LongBitMapTest {

    @Test
    public void test() {
        LongBitMap longBitMap = LongBitMap.of();
        Assert.assertFalse(longBitMap.contains(1L));
        longBitMap.add(1L);
        Assert.assertTrue(longBitMap.contains(1L));
        longBitMap.add(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        Assert.assertEquals(longBitMap.size(), 10L);
        longBitMap.remove(1L);
        Assert.assertEquals(longBitMap.size(), 9L);
        longBitMap.clear();
        Assert.assertTrue(longBitMap.isEmpty());
    }
}