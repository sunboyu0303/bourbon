package com.github.bourbon.springframework.boot.context.properties.bind.handler;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.bind.AbstractBindHandler;
import com.github.bourbon.springframework.boot.context.properties.bind.BindContext;
import com.github.bourbon.springframework.boot.context.properties.bind.BindHandler;
import com.github.bourbon.springframework.boot.context.properties.bind.Bindable;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;

import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 16:53
 */
public class IgnoreErrorsBindHandler extends AbstractBindHandler {

    public IgnoreErrorsBindHandler() {
    }

    public IgnoreErrorsBindHandler(BindHandler parent) {
        super(parent);
    }

    @Override
    public Object onFailure(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Exception error) {
        return ObjectUtils.defaultIfNull(target.getValue(), Supplier::get);
    }
}