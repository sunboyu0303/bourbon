package com.github.bourbon.base.system;

import com.github.bourbon.base.utils.LocalHostUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/13 17:37
 */
public class SystemInfoTest {

    @Test
    public void test() {
        Assert.assertEquals(SystemInfo.hostInfo.getIp(), LocalHostUtils.ip());
        Assert.assertEquals(SystemInfo.hostInfo.getHostName(), LocalHostUtils.hostName());

        Assert.assertNotNull(SystemInfo.appInfo.getAppId());
        Assert.assertNotNull(SystemInfo.appInfo.getAppName());
    }
}