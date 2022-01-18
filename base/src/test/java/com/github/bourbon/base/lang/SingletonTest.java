package com.github.bourbon.base.lang;

import com.github.bourbon.base.utils.ListUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/15 10:40
 */
public class SingletonTest {

    @Test
    public void test() {
        List<String> list = ListUtils.newArrayList();
        list.add("1");
        org.junit.Assert.assertNull(Singleton.get(ArrayList.class));
        Singleton.put(list);
        org.junit.Assert.assertNotNull(Singleton.get(ArrayList.class));
    }
}