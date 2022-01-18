package com.github.bourbon.base.lang;

import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/19 11:28
 */
public class AssertTest {

    @Test
    public void test() {
        int[] array = new int[0];
        org.junit.Assert.assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(array, ""));
    }
}