package com.github.bourbon.base.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/22 15:26
 */
public class ArrayUtilsTest {

    @Test
    public void test() {
        Integer[] src = {1, 2, 3, 4, 5};
        Integer[] dest = new Integer[src.length + 6];
        System.arraycopy(src, 0, dest, 0, src.length);
        Assert.assertEquals(ArrayUtils.toString(dest), ArrayUtils.toString(ArrayUtils.expand(src, 6)));
    }
}