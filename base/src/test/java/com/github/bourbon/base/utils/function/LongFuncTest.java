package com.github.bourbon.base.utils.function;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 18:49
 */
public class LongFuncTest {

    @Test
    public void test() {
        LongFunc<String> func = s -> Integer.valueOf(s).longValue();
        Assert.assertEquals(func.apply("123"), 123L);
    }
}