package com.github.bourbon.springframework.beans.factory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 16:08
 */
public interface BeanClassLoaderAware extends Aware {

    void setBeanClassLoader(ClassLoader classLoader);
}