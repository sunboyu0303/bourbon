package com.github.bourbon.springframework.boot.context.properties.bind;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 13:49
 */
public class DataObjectPropertyNameTest {

    @Test
    public void test() {
        Assert.assertEquals(DataObjectPropertyName.toDashedForm("short-name"), "short-name");
        Assert.assertEquals(DataObjectPropertyName.toDashedForm("short_name"), "short-name");
        Assert.assertEquals(DataObjectPropertyName.toDashedForm("shortName"), "short-name");
    }
}