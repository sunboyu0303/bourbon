package com.github.bourbon.base.utils.function;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 23:09
 */
public class IntFuncTest {

    @Test
    public void test() {
        IntFunc<String> intFunc = String::hashCode;
        Assert.assertEquals(intFunc.apply("1234567890"), -2054162789);
    }
}