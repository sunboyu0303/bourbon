package com.github.bourbon.springframework.context;

import com.github.bourbon.springframework.core.io.XPathProtocolResolver;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/27 23:27
 */
public class ResourceLoaderPostProcessor implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String configPath = environment.getProperty("CONF_PATH");
        if (configPath == null) {
            configPath = environment.getProperty("config.path");
        }
        applicationContext.addProtocolResolver(new XPathProtocolResolver(configPath));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 100;
    }
}