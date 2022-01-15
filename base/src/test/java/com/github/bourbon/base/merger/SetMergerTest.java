package com.github.bourbon.base.merger;

import com.github.bourbon.base.utils.SetUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 00:24
 */
public class SetMergerTest {

    @Test
    public void test() {
        Set<Object>[] sets = new Set[]{
                SetUtils.newHashSet(false),
                SetUtils.newHashSet(true, false),
                SetUtils.newHashSet(true, false, true),
                SetUtils.newHashSet(false, true, false, true)
        };
        Assert.assertEquals(new SetMerger().merge(sets).size(), 2);
    }
}