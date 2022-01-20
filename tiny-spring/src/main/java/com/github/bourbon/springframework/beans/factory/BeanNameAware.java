package com.github.bourbon.springframework.beans.factory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 16:07
 */
public interface BeanNameAware extends Aware {

    void setBeanName(String name);
}