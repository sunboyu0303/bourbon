package com.github.bourbon.base.utils;

import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/21 14:30
 */
public class JVMUtilsTest {

    @Test
    public void test() {
        JVMUtils.dumpJStack();
        TimeUnitUtils.sleepMinutes(1L);
    }
}