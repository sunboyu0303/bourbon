package com.github.bourbon.springframework.cache.core.annotation;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.extension.support.ExtensionLoader;
import com.github.bourbon.cache.core.CacheAdapter;

import java.lang.reflect.Method;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/14 14:53
 */
public interface CacheAspect {

    ExtensionLoader<CacheAdapter> loader = ScopeModelUtils.getExtensionLoader(CacheAdapter.class);

    static String getKey(Method m) {
        return m.getDeclaringClass().getName() + StringConstants.DOLLAR + m.getName() + StringConstants.DOLLAR + m.toString();
    }
}