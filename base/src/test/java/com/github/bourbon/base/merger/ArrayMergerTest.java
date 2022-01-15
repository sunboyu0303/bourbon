package com.github.bourbon.base.merger;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/15 23:52
 */
public class ArrayMergerTest {

    @Test
    public void test() {
        Object[][] objects = {
                {"1"},
                {"2", "3"},
                {"4", "5", "6"},
                {"7", "8", "9", "0"}
        };
        Assert.assertEquals(new ArrayMerger().merge(objects).length, 10);
    }
}