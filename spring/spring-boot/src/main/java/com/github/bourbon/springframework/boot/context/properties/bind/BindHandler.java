package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 15:06
 */
public interface BindHandler {

    BindHandler DEFAULT = new BindHandler() {};

    default <T> Bindable<T> onStart(ConfigurationPropertyName name, Bindable<T> target, BindContext context) {
        return target;
    }

    default Object onSuccess(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) {
        return result;
    }

    default Object onCreate(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) {
        return result;
    }

    default Object onFailure(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Exception error) throws Exception {
        throw error;
    }

    default void onFinish(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) throws Exception {
    }
}