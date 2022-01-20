package com.github.bourbon.springframework.beans.factory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 00:20
 */
public interface DisposableBean {

    String NAME = "destroy";

    void destroy() throws Exception;
}