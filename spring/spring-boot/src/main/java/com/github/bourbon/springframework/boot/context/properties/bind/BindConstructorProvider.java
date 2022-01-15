package com.github.bourbon.springframework.boot.context.properties.bind;

import java.lang.reflect.Constructor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 21:54
 */
@FunctionalInterface
public interface BindConstructorProvider {

    BindConstructorProvider DEFAULT = new DefaultBindConstructorProvider();

    Constructor<?> getBindConstructor(Bindable<?> bindable, boolean isNestedConstructorBinding);
}