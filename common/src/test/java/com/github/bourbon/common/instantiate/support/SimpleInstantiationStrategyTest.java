package com.github.bourbon.common.instantiate.support;

import com.github.bourbon.common.instantiate.InstantiationStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 14:04
 */
public class SimpleInstantiationStrategyTest {

    @Test
    public void test() throws Exception {
        InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();
        final Object instantiate = instantiationStrategy.instantiate(ArrayList.class, null, null);
        Assert.assertTrue(instantiate instanceof List);
    }
}