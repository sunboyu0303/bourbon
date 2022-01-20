package com.github.bourbon.springframework.beans.factory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 00:20
 */
public interface InitializingBean {

    String NAME = "afterPropertiesSet";

    void afterPropertiesSet() throws Exception;
}