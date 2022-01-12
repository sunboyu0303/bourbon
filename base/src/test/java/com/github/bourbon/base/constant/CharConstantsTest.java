package com.github.bourbon.base.constant;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/13 17:21
 */
public class CharConstantsTest {

    @Test
    public void test() {
        Assert.assertEquals(45, CharConstants.HYPHEN);
        Assert.assertEquals(46, CharConstants.DOT);
        Assert.assertEquals(64, CharConstants.AT);
        Assert.assertEquals(95, CharConstants.UNDERLINE);
    }
}