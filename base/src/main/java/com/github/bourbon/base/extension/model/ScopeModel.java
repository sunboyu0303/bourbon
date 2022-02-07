package com.github.bourbon.base.extension.model;

import com.github.bourbon.base.extension.ExtensionAccessor;
import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.support.ExtensionDirector;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.base.utils.concurrent.ConcurrentHashSet;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 15:37
 */
public abstract class ScopeModel implements ExtensionAccessor {

    private final Set<ClassLoader> classLoaders = new ConcurrentHashSet<>();
    private final ScopeModel parent;
    private final ExtensionDirector extensionDirector;
    private final ScopeModelAccessor scopeModelAccessor;

    protected ScopeModel(ScopeModel parent, ExtensionScope scope) {
        this.parent = parent;
        this.extensionDirector = new ExtensionDirector(ObjectUtils.defaultIfNullElseFunction(parent, ScopeModel::getExtensionDirector), scope, this);
        this.scopeModelAccessor = new SimpleScopeModelAccessor(this);
        this.addClassLoader(getClass().getClassLoader());
    }

    protected void addClassLoader(ClassLoader classLoader) {
        classLoaders.add(classLoader);
        if (parent != null) {
            parent.addClassLoader(classLoader);
        }
    }

    public Set<ClassLoader> getClassLoaders() {
        return SetUtils.unmodifiableSet(classLoaders);
    }

    public ScopeModelAccessor getScopeModelAccessor() {
        return scopeModelAccessor;
    }

    @Override
    public ExtensionDirector getExtensionDirector() {
        return extensionDirector;
    }
}