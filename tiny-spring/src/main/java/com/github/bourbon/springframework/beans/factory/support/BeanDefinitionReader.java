package com.github.bourbon.springframework.beans.factory.support;

import com.github.bourbon.base.io.Resource;
import com.github.bourbon.base.io.ResourceLoader;
import com.github.bourbon.springframework.beans.BeansException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 15:40
 */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException;

    void loadBeanDefinitions(Resource... resources) throws BeansException;

    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(String... locations) throws BeansException;
}