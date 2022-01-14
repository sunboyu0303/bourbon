package com.github.bourbon.cache.multi.support;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.cache.core.Cache;
import com.github.bourbon.cache.core.CacheAdapter;
import com.github.bourbon.cache.core.CacheParamInfo;
import com.github.bourbon.cache.core.utils.MemoryUnit;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 18:41
 */
public class MultiCacheAdapterTest {

    @Test
    public void test() throws InterruptedException {
        CacheAdapter adapter = ScopeModelUtils.getExtensionLoader(CacheAdapter.class).getExtension("multi");
        CacheParamInfo<String, String> info = new CacheParamInfo<String, String>()
                .setMaximumSize(1024L)
                .setJvmDuration(10L)
                .setConcurrencyLevel(16)
                .setInitialCapacity(16)
                .setTimeUnit(TimeUnit.SECONDS)
                .setAsync(false)
                .setThreadPoolSize(4)
                .setApplyCache(() -> true)
                .setUniqueIdentifier("k1")
                .setCallBack(String::toUpperCase)
                .setKeyType(String.class)
                .setMemoryUnit(MemoryUnit.MB)
                .setMemorySize(16)
                .setOhcDuration(30)
                .setEmptyTimeout(10)
                .setTimeout(20);
        Cache<String, String> cache = adapter.getCache(info);
        cache.get(StringConstants.SMALL_K);
        cache.get(StringConstants.SMALL_K);
        cache.get(StringConstants.SMALL_K);
        cache.get(StringConstants.SMALL_K);
        TimeUnit.SECONDS.sleep(10L);
        Assert.assertEquals(cache.get(StringConstants.SMALL_K), StringConstants.K);
    }
}