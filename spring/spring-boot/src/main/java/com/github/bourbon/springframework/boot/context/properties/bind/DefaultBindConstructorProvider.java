package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.utils.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.KotlinDetector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 21:55
 */
class DefaultBindConstructorProvider implements BindConstructorProvider {

    @Override
    public Constructor<?> getBindConstructor(Bindable<?> bindable, boolean isNestedConstructorBinding) {
        Class<?> type = bindable.getType().resolve();
        if (bindable.getValue() != null || type == null) {
            return null;
        }
        if (KotlinDetector.isKotlinPresent() && KotlinDetector.isKotlinType(type)) {
            return getDeducedKotlinConstructor(type);
        }
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if (constructors.length == 1 && constructors[0].getParameterCount() > 0) {
            return constructors[0];
        }
        Constructor<?> constructor = null;
        for (Constructor<?> candidate : constructors) {
            if (!Modifier.isPrivate(candidate.getModifiers())) {
                if (constructor != null) {
                    return null;
                }
                constructor = candidate;
            }
        }
        if (constructor != null && constructor.getParameterCount() > 0) {
            return constructor;
        }
        return null;
    }

    private Constructor<?> getDeducedKotlinConstructor(Class<?> type) {
        return BooleanUtils.defaultIfPredicate(BeanUtils.findPrimaryConstructor(type), p -> p != null && p.getParameterCount() > 0, p -> p);
    }
}