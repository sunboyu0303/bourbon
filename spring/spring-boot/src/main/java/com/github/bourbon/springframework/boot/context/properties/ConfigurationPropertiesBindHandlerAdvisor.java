package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.springframework.boot.context.properties.bind.BindHandler;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 20:05
 */
@FunctionalInterface
public interface ConfigurationPropertiesBindHandlerAdvisor {

    BindHandler apply(BindHandler bindHandler);
}