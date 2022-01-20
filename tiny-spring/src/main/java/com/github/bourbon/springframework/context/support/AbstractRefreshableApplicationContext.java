package com.github.bourbon.springframework.context.support;

import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 23:33
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        loadBeanDefinitions(bf);
        beanFactory = bf;
    }

    @Override
    protected DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory bf);
}