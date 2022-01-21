package com.github.bourbon.base.lang.cache;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

public class LRUCacheTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        LRUCache<Integer, Integer> lruCache = new LRUCache<>(32);
        for (int i = 0; i < 64; i++) {
            lruCache.put(i, i);
            logger.info(lruCache);
        }
        Assert.assertFalse(lruCache.containsKey(0));
    }
}