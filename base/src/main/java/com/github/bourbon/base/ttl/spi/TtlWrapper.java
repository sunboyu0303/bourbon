package com.github.bourbon.base.ttl.spi;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/11 23:13
 */
public interface TtlWrapper<T> extends TtlEnhanced {

    T unwrap();
}