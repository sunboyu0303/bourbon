package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.bind.BindConstructorProvider;
import com.github.bourbon.springframework.boot.context.properties.bind.Bindable;
import org.springframework.beans.BeanUtils;
import org.springframework.core.KotlinDetector;
import org.springframework.core.annotation.MergedAnnotations;

import java.lang.reflect.Constructor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 17:52
 */
class ConfigurationPropertiesBindConstructorProvider implements BindConstructorProvider {

    static final ConfigurationPropertiesBindConstructorProvider INSTANCE = new ConfigurationPropertiesBindConstructorProvider();

    @Override
    public Constructor<?> getBindConstructor(Bindable<?> bindable, boolean isNestedConstructorBinding) {
        return getBindConstructor(bindable.getType().resolve(), isNestedConstructorBinding);
    }

    Constructor<?> getBindConstructor(Class<?> type, boolean isNestedConstructorBinding) {
        return ObjectUtils.defaultIfNull(type, t -> {
            Constructor<?> constructor = findConstructorBindingAnnotatedConstructor(t);
            if (constructor == null && (isConstructorBindingType(t) || isNestedConstructorBinding)) {
                constructor = deduceBindConstructor(t);
            }
            return constructor;
        });
    }

    private Constructor<?> findConstructorBindingAnnotatedConstructor(Class<?> type) {
        if (isKotlinType(type)) {
            Constructor<?> constructor = BeanUtils.findPrimaryConstructor(type);
            if (constructor != null) {
                return findAnnotatedConstructor(type, constructor);
            }
        }
        return findAnnotatedConstructor(type, type.getDeclaredConstructors());
    }

    private Constructor<?> findAnnotatedConstructor(Class<?> type, Constructor<?>... candidates) {
        Constructor<?> constructor = null;
        for (Constructor<?> candidate : candidates) {
            if (MergedAnnotations.from(candidate).isPresent(ConstructorBinding.class)) {
                Assert.isTrue(candidate.getParameterCount() > 0, () -> type.getName() + " declares @ConstructorBinding on a no-args constructor");
                Assert.isNull(constructor, () -> type.getName() + " has more than one @ConstructorBinding constructor");
                constructor = candidate;
            }
        }
        return constructor;
    }

    private boolean isConstructorBindingType(Class<?> type) {
        return isImplicitConstructorBindingType(type) || isConstructorBindingAnnotatedType(type);
    }

    private boolean isImplicitConstructorBindingType(Class<?> type) {
        return ObjectUtils.defaultIfNull(type.getSuperclass(), c -> "java.lang.Record".equals(c.getName()), false);
    }

    private boolean isConstructorBindingAnnotatedType(Class<?> type) {
        return MergedAnnotations.from(type, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY_AND_ENCLOSING_CLASSES).isPresent(ConstructorBinding.class);
    }

    private Constructor<?> deduceBindConstructor(Class<?> type) {
        return BooleanUtils.defaultSupplierIfFalse(isKotlinType(type), () -> deducedKotlinBindConstructor(type),
                () -> BooleanUtils.defaultIfPredicate(type.getDeclaredConstructors(), c -> c.length == 1, cs -> BooleanUtils.defaultIfPredicate(cs[0], c -> c.getParameterCount() > 0, t -> t))
        );
    }

    private Constructor<?> deducedKotlinBindConstructor(Class<?> type) {
        return ObjectUtils.defaultIfNull(BeanUtils.findPrimaryConstructor(type), p -> BooleanUtils.defaultIfFalse(p.getParameterCount() > 0, () -> p));
    }

    private boolean isKotlinType(Class<?> type) {
        return KotlinDetector.isKotlinPresent() && KotlinDetector.isKotlinType(type);
    }
}