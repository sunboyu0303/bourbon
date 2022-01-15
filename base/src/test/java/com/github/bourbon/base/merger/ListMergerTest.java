package com.github.bourbon.base.merger;

import com.github.bourbon.base.utils.ListUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/16 00:13
 */
public class ListMergerTest {

    @Test
    public void test() {
        List<Object>[] lists = new List[]{
                ListUtils.newArrayList("1"),
                ListUtils.newArrayList("2", "3"),
                ListUtils.newArrayList("4", "5", "6"),
                ListUtils.newArrayList("7", "8", "9", "0")
        };
        Assert.assertEquals(new ListMerger().merge(lists).toString(), "[1, 2, 3, 4, 5, 6, 7, 8, 9, 0]");
    }
}