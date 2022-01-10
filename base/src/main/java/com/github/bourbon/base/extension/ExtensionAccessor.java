package com.github.bourbon.base.extension;

import com.github.bourbon.base.extension.support.ExtensionDirector;
import com.github.bourbon.base.extension.support.ExtensionLoader;
import com.github.bourbon.base.utils.ObjectUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 14:32
 */
public interface ExtensionAccessor {

    ExtensionDirector getExtensionDirector();

    default <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return getExtensionDirector().getExtensionLoader(type);
    }

    default <T> T getExtension(Class<T> type, String name) {
        return ObjectUtils.defaultIfNull(getExtensionLoader(type), e -> e.getExtension(name));
    }

    default <T> T getDefaultExtension(Class<T> type) {
        return ObjectUtils.defaultIfNull(getExtensionLoader(type), ExtensionLoader::getDefaultExtension);
    }
}