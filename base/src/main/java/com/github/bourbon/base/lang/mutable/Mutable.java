package com.github.bourbon.base.lang.mutable;

import java.io.Serializable;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 15:43
 */
public interface Mutable<T> extends Serializable {

    T get();

    void set(T t);
}