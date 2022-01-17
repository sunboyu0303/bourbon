package com.github.bourbon.base.extension.support;

import com.github.bourbon.base.extension.model.*;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.SystemLogger;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 21:14
 */
public class InstantiationStrategy {

    private final Logger logger = new SystemLogger();

    private ScopeModelAccessor scopeModelAccessor;

    public InstantiationStrategy(ScopeModelAccessor scopeModelAccessor) {
        this.scopeModelAccessor = scopeModelAccessor;
    }

    @SuppressWarnings("unchecked")
    public <T> T instantiate(Class<T> type) throws ReflectiveOperationException {
        try {
            return type.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            logger.error(e);
        }

        List<Constructor<?>> list = Arrays.stream(type.getConstructors()).filter(this::isMatched).collect(Collectors.toList());
        if (list.size() > 1) {
            throw new IllegalArgumentException("Expect only one but found " + list.size() + " matched constructors for type: " + type.getName() + ", matched constructors: " + list);
        } else if (list.isEmpty()) {
            throw new IllegalArgumentException("None matched constructor was found for type: " + type.getName());
        }

        Constructor<?> constructor = list.get(0);
        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = getArgumentValueForType(parameterTypes[i]);
        }
        return (T) constructor.newInstance(args);
    }

    private boolean isMatched(Constructor<?> constructor) {
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            if (!ScopeModel.class.isAssignableFrom(parameterType)) {
                return false;
            }
        }
        return true;
    }

    private Object getArgumentValueForType(Class<?> parameterType) {
        if (scopeModelAccessor != null) {
            if (parameterType == ScopeModel.class) {
                return scopeModelAccessor.getScopeModel();
            }
            if (parameterType == FrameworkModel.class) {
                return scopeModelAccessor.getFrameworkModel();
            }
            if (parameterType == ApplicationModel.class) {
                return scopeModelAccessor.getApplicationModel();
            }
            if (parameterType == ModuleModel.class) {
                return scopeModelAccessor.getModuleModel();
            }
        }
        return null;
    }
}