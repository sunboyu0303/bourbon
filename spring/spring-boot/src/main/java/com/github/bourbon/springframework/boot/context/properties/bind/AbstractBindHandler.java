package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 19:48
 */
public abstract class AbstractBindHandler implements BindHandler {

    private final BindHandler parent;

    public AbstractBindHandler() {
        this(BindHandler.DEFAULT);
    }

    public AbstractBindHandler(BindHandler parent) {
        this.parent = ObjectUtils.requireNonNull(parent, "Parent must not be null");
    }

    @Override
    public <T> Bindable<T> onStart(ConfigurationPropertyName name, Bindable<T> target, BindContext context) {
        return parent.onStart(name, target, context);
    }

    @Override
    public Object onSuccess(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) {
        return parent.onSuccess(name, target, context, result);
    }

    @Override
    public Object onFailure(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Exception error) throws Exception {
        return parent.onFailure(name, target, context, error);
    }

    @Override
    public void onFinish(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) throws Exception {
        parent.onFinish(name, target, context, result);
    }
}