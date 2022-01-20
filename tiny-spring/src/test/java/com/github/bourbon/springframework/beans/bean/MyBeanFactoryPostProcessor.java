package com.github.bourbon.springframework.beans.bean;

import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.PropertyValue;
import com.github.bourbon.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.github.bourbon.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 14:32
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.getBeanDefinition("userService").getPropertyValues().addPropertyValue(PropertyValue.of("company", "改为：字节跳动"));
    }
}