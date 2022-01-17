package com.github.bourbon.springframework.boot.autoconfigure.utils;

import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.springframework.boot.autoconfigure.condition.ConditionalOnProfile;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/17 13:57
 */
public class ClassUtilsTest {

    @Test
    public void test() {
        Assert.assertEquals(ClassUtils.getSimpleClassName(ConditionalOnProfile.class), org.springframework.util.ClassUtils.getShortName(ConditionalOnProfile.class));
    }
}