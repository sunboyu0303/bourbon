package com.github.bourbon.base.io;

import com.github.bourbon.base.extension.model.ScopeModelUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 14:50
 */
public class ResourceLoaderTest {

    private ResourceLoader resourceLoader;

    @Before
    public void init() {
        resourceLoader = ScopeModelUtils.getExtensionLoader(ResourceLoader.class).getDefaultExtension();
    }

    @Test
    public void test() throws IOException {
        Properties properties = new Properties();
        properties.load(resourceLoader.getResource("classpath:META-INF/app.properties").getInputStream());
        String s = properties.toString();
        properties = new Properties();
        properties.load(resourceLoader.getResource("src/test/resources/META-INF/app.properties").getInputStream());
        Assert.assertEquals(s, properties.toString());
    }
}