package com.github.bourbon.springframework.boot.context.properties.bind.handler;

import com.github.bourbon.springframework.boot.context.properties.bind.AbstractBindHandler;
import com.github.bourbon.springframework.boot.context.properties.bind.BindContext;
import com.github.bourbon.springframework.boot.context.properties.bind.BindHandler;
import com.github.bourbon.springframework.boot.context.properties.bind.Bindable;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.core.convert.ConverterNotFoundException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 16:49
 */
public class IgnoreTopLevelConverterNotFoundBindHandler extends AbstractBindHandler {

    public IgnoreTopLevelConverterNotFoundBindHandler() {
    }

    public IgnoreTopLevelConverterNotFoundBindHandler(BindHandler parent) {
        super(parent);
    }

    @Override
    public Object onFailure(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Exception error) throws Exception {
        if (context.getDepth() == 0 && error instanceof ConverterNotFoundException) {
            return null;
        }
        throw error;
    }
}