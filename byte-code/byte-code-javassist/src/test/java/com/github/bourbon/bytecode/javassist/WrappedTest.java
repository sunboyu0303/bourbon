package com.github.bourbon.bytecode.javassist;

import com.github.bourbon.bytecode.javassist.support.Wrapped;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 00:20
 */
public class WrappedTest {

    @Test
    public void test() throws Exception {
        BizService test = new Wrapped<>(BizServiceImpl.class).getObject();
        Assert.assertNotNull(test.sum("1", "5"));
    }
}