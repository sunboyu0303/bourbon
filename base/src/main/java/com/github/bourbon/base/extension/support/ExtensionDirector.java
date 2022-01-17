package com.github.bourbon.base.extension.support;

import com.github.bourbon.base.extension.ExtensionAccessor;
import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;
import com.github.bourbon.base.extension.model.ScopeModel;
import com.github.bourbon.base.utils.BooleanUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 14:35
 */
public class ExtensionDirector implements ExtensionAccessor {

    private final ConcurrentMap<Class<?>, ExtensionLoader<?>> extensionLoadersMap = new ConcurrentHashMap<>(64);
    private final ExtensionDirector parent;
    private final ExtensionScope scope;
    private final ScopeModel scopeModel;

    public ExtensionDirector(ExtensionDirector parent, ExtensionScope scope, ScopeModel scopeModel) {
        this.parent = parent;
        this.scope = scope;
        this.scopeModel = scopeModel;
    }

    @Override
    public ExtensionDirector getExtensionDirector() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an interface!");
        }
        if (!type.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an extension, because it is NOT annotated with @SPI!");
        }

        ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoadersMap.get(type);
        if (loader == null && type.getAnnotation(SPI.class).scope() == ExtensionScope.SELF) {
            loader = createExtensionLoader0(type);
        }
        if (loader == null) {
            if (parent != null) {
                loader = parent.getExtensionLoader(type);
            }
        }
        if (loader == null) {
            loader = createExtensionLoader(type);
        }
        return loader;
    }

    private <T> ExtensionLoader<T> createExtensionLoader(Class<T> type) {
        return BooleanUtils.defaultIfFalse(type.getAnnotation(SPI.class).scope() == scope, () -> createExtensionLoader0(type));
    }

    @SuppressWarnings("unchecked")
    private <T> ExtensionLoader<T> createExtensionLoader0(Class<T> type) {
        return (ExtensionLoader<T>) extensionLoadersMap.computeIfAbsent(type, o -> new ExtensionLoader<>(o, this, scopeModel));
    }
}