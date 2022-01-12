package com.github.bourbon.uuid.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/13 10:02
 */
public class UUIDPropertiesTest {

    @Test
    public void test() {
        Assert.assertEquals(UUIDProperties.get("1"), "1");
        Assert.assertEquals(UUIDProperties.getInteger("2", -1), 2);
        Assert.assertEquals(UUIDProperties.getLong("3", -1), 3L);
        Assert.assertNull(UUIDProperties.get("7"));
    }
}