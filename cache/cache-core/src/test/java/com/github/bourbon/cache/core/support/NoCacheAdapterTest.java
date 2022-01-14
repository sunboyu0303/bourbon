package com.github.bourbon.cache.core.support;

import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheAdapter;
import com.github.bourbon.cache.core.CacheParamInfo;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 10:40
 */
public class NoCacheAdapterTest {

    @Test
    public void test() {
        CacheAdapter noCacheAdapter = new NoCacheAdapter();
        Cache<String, String> noCache = noCacheAdapter.getCache(new CacheParamInfo<>());
        Assert.assertNull(noCache.get("2"));
        noCache.put("2", "2");
        Assert.assertNull(noCache.get("2"));
    }
}