package com.github.bourbon.springframework.context;

import com.github.bourbon.base.io.ResourceLoader;
import com.github.bourbon.springframework.beans.factory.HierarchicalBeanFactory;
import com.github.bourbon.springframework.beans.factory.ListableBeanFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 22:19
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}