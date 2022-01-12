package com.github.bourbon.springframework.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/13 17:02
 */
public class SpringPropertiesTest {

    @Test
    public void test() {
        Assert.assertNotNull(SpringProperties.getProperty("1"));
        Assert.assertNull(SpringProperties.getProperty("2"));
        SpringProperties.setProperty("2", "true");
        Assert.assertTrue(SpringProperties.getFlag("2"));
    }
}