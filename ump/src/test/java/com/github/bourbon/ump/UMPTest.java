package com.github.bourbon.ump;

import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.extension.support.ExtensionLoader;
import com.github.bourbon.base.logger.SystemLogger;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 17:11
 */
public class UMPTest {

    @Test
    public void testRuntimeEnvironmentService() {
        ExtensionLoader<RuntimeEnvironmentService> loader = ScopeModelUtils.getExtensionLoader(RuntimeEnvironmentService.class);
        Assert.assertNotNull(loader);
        new SystemLogger().error(loader.getSupportedExtensionInstances());

        Profiler.initHeartBeats("testInitHeartBeats");
        Profiler.registerJVMInfo("jvm-key");
    }

    @Test
    public void testUmpLoggerFactory() {
        UmpLoggerFactory factory = ScopeModelUtils.getExtensionLoader(UmpLoggerFactory.class).getDefaultExtension();
        Assert.assertNotNull(factory);
        Assert.assertNotNull(factory.jvmLogger());
        factory.jvmLogger().error("123123");
    }
}