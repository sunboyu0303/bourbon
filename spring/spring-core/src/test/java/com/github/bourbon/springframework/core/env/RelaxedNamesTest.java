package com.github.bourbon.springframework.core.env;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/30 17:08
 */
public class RelaxedNamesTest {

    @Test
    public void test() {
        Assert.assertEquals(Arrays.stream(RelaxedNames.Variation.values()).map(v -> v.apply("className")).collect(Collectors.toSet()).size(), 3);
    }
}