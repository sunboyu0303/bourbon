package com.github.bourbon.base.system;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/24 17:25
 */
public class VersionTest {

    @Test
    public void testVersion() {
        Assert.assertNotNull(Version.getVersion(List.class));
    }
}