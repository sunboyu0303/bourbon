package com.github.bourbon.cache.core.support;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 10:33
 */
public class NoCacheTest {

    @Test
    public void test() {
        NoCache<String, String> noCache = new NoCache<>();
        Assert.assertNull(noCache.get("1"));
        noCache.put("1", "1");
        Assert.assertNull(noCache.get("1"));
    }
}