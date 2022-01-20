package com.github.bourbon.springframework.context.support;

import com.github.bourbon.base.utils.ArrayUtils;
import com.github.bourbon.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.github.bourbon.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 23:37
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory bf) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf, this);
        String[] configLocations = getConfigLocations();
        if (!ArrayUtils.isEmpty(configLocations)) {
            reader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getConfigLocations();
}