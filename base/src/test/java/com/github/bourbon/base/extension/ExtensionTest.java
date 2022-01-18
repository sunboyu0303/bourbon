package com.github.bourbon.base.extension;

import com.github.bourbon.base.convert.Converter;
import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.extension.support.ExtensionLoader;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 10:46
 */
public class ExtensionTest {

    @Test
    public void test() {
        ExtensionLoader<Converter> loader = ScopeModelUtils.getExtensionLoader(Converter.class);
        Assert.assertNotEquals(loader.getExtension("string-to-character").convert("x", '0'), '0');
    }
}