package com.github.bourbon.common.bloomfilter;

import com.github.bourbon.base.bloomfilter.BloomFilter;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.ListUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 11:23
 */
public class BitMapBloomFilterTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        BloomFilter bloomFilter = new BitMapBloomFilter();
        int size = 1000000;
        long startTime = System.nanoTime();
        for (int i = 0; i < size; i++) {
            bloomFilter.add("" + i);
        }
        long endTime = System.nanoTime();
        logger.info("程序运行时间：{} 纳秒", (endTime - startTime));

        startTime = System.nanoTime();
        if (bloomFilter.contains("" + 29999)) {
            logger.info("命中了");
        }
        endTime = System.nanoTime();
        logger.info("程序运行时间：{} 纳秒", (endTime - startTime));

        List<Integer> list = ListUtils.newArrayList(1000);
        for (int i = size + 10000; i < size + 20000; i++) {
            if (bloomFilter.contains("" + i)) {
                list.add(i);
            }
        }
        Assert.assertEquals(list.size(), 0);
    }
}