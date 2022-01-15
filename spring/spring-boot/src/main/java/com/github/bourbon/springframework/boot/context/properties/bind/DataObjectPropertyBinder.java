package com.github.bourbon.springframework.boot.context.properties.bind;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 14:16
 */
interface DataObjectPropertyBinder {

    Object bindProperty(String propertyName, Bindable<?> target);
}