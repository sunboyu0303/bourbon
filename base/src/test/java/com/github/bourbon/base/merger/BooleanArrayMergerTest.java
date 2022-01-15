package com.github.bourbon.base.merger;

import com.github.bourbon.base.utils.PrimitiveArrayUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 00:01
 */
public class BooleanArrayMergerTest {

    @Test
    public void test() {
        boolean[][] booleans = {
                {false},
                {true, false},
                {true, false, true},
                {false, true, false, true}
        };
        Assert.assertEquals(SetUtils.newHashSet(PrimitiveArrayUtils.wrap(new BooleanArrayMerger().merge(booleans))).size(), 2);
    }
}