package com.github.bourbon.bytecode.core.constant;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/12 16:23
 */
public class ByteCodeConstantsTest {

    @Test
    public void test() {
        Assert.assertEquals("Proxy", ByteCodeConstants.PROXY);
        Assert.assertEquals("Proxy.class", ByteCodeConstants.PROXY_CLASS);
    }
}