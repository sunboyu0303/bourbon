package com.github.bourbon.springframework.beans.factory;

import com.github.bourbon.springframework.beans.BeansException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/9 11:15
 */
public interface ObjectFactory<T> {

    T getObject() throws BeansException;
}