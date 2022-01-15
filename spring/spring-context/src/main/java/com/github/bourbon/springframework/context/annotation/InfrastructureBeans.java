package com.github.bourbon.springframework.context.annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/31 10:51
 */
public class InfrastructureBeans {

    public static void register(BeanDefinitionRegistry registry) {
        ApplicationContextUtils.init(registry);
        ConditionContextUtils.init(registry);
        SpringContextUtils.init(registry);

        BeanDefinitionRegistryUtils.register(registry);
        PropertiesBeanUtils.register(registry);
    }

    private InfrastructureBeans() {
    }
}