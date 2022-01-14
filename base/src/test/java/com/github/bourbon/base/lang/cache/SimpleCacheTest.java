package com.github.bourbon.base.lang.cache;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 09:57
 */
public class SimpleCacheTest {

    @Test
    public void test() {
        SimpleCache<Integer, String> cache = new SimpleCache<>();
        Assert.assertNotNull(cache.get(1, () -> "1"));
        Assert.assertNotNull(cache.put(1, "2"));
        Assert.assertEquals(cache.get(1), "2");
        Assert.assertNotNull(cache.computeIfAbsent(1, Object::toString));
        Assert.assertNotEquals(cache.get(1), "1");
        cache.clear();
        Assert.assertNotNull(cache.computeIfAbsent(1, Object::toString));
        Assert.assertEquals(cache.get(1), "1");
    }
}