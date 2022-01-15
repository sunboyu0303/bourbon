package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/20 14:09
 */
interface DataObjectBinder {

    <T> T bind(ConfigurationPropertyName name, Bindable<T> target, Binder.Context context, DataObjectPropertyBinder propertyBinder);

    <T> T create(Bindable<T> target, Binder.Context context);
}