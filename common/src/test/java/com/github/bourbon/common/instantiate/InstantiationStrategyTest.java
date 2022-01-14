package com.github.bourbon.common.instantiate;

import com.github.bourbon.base.extension.model.ScopeModelUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 14:03
 */
public class InstantiationStrategyTest {

    @Test
    public void test() throws Exception {
        InstantiationStrategy instantiationStrategy = ScopeModelUtils.getExtensionLoader(InstantiationStrategy.class).getDefaultExtension();
        Object instantiate = instantiationStrategy.instantiate(ArrayList.class, null, null);
        Assert.assertEquals(instantiate.getClass().getName(), ArrayList.class.getName());
        instantiationStrategy = ScopeModelUtils.getExtensionLoader(InstantiationStrategy.class).getExtension("cglib");
        instantiate = instantiationStrategy.instantiate(ArrayList.class, null, null);
        Assert.assertNotEquals(instantiate.getClass().getName(), ArrayList.class.getName());
    }
}