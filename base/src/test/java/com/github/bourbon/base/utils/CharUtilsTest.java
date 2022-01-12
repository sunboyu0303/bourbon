package com.github.bourbon.base.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/13 16:14
 */
public class CharUtilsTest {

    @Test
    public void test() {
        Assert.assertTrue(CharUtils.isNumber('1'));
        Assert.assertFalse(CharUtils.isNumber('a'));
    }
}