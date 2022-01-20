package com.github.bourbon.base.utils;

import java.lang.reflect.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 10:39
 */
public interface TypeUtils {

    static boolean isClass(Type type) {
        return type instanceof Class;
    }

    static boolean isParameterizedType(Type type) {
        return type instanceof ParameterizedType;
    }

    static boolean isTypeVariable(Type type) {
        return type instanceof TypeVariable;
    }

    static boolean isWildcardType(Type type) {
        return type instanceof WildcardType;
    }

    @SuppressWarnings("unchecked")
    static <T> Class<T> getClass(Type type) {
        if (ObjectUtils.nonNull(type)) {
            if (isClass(type)) {
                return (Class) type;
            }
            if (isParameterizedType(type)) {
                return (Class) ((ParameterizedType) type).getRawType();
            }
            if (isTypeVariable(type)) {
                return (Class) ((TypeVariable) type).getBounds()[0];
            }
            if (isWildcardType(type)) {
                Type[] upperBounds = ((WildcardType) type).getUpperBounds();
                if (upperBounds.length == 1) {
                    return getClass(upperBounds[0]);
                }
            }
        }
        return null;
    }

    static Type getType(Field field) {
        return ObjectUtils.defaultIfNull(field, Field::getGenericType);
    }

    static Type getType(Class<?> c, String s) {
        return getType(ReflectUtils.getField(c, s));
    }

    static Class<?> getClass(Field field) {
        return ObjectUtils.defaultIfNull(field, Field::getType);
    }

    static Class<?> getClass(Class<?> c, String s) {
        return getClass(ReflectUtils.getField(c, s));
    }

    static Type getFirstParamType(Method m) {
        return getParamType(m, 0);
    }

    static Type getParamType(Method m, int i) {
        return BooleanUtils.defaultIfPredicate(getParamTypes(m), t -> ObjectUtils.nonNull(t) && t.length > i, t -> t[i]);
    }

    static Type[] getParamTypes(Method method) {
        return ObjectUtils.defaultIfNull(method, Method::getGenericParameterTypes);
    }

    static Class<?> getFirstParamClass(Method m) {
        return getParamClass(m, 0);
    }

    static Class<?> getParamClass(Method m, int i) {
        return BooleanUtils.defaultIfPredicate(getParamClasses(m), c -> ObjectUtils.nonNull(c) && c.length > i, c -> c[i]);
    }

    static Class<?>[] getParamClasses(Method method) {
        return ObjectUtils.defaultIfNull(method, Method::getParameterTypes);
    }

    static Type getReturnType(Method method) {
        return ObjectUtils.defaultIfNull(method, Method::getGenericReturnType);
    }

    static Class<?> getReturnClass(Method method) {
        return ObjectUtils.defaultIfNull(method, Method::getReturnType);
    }

    static Type getTypeArgument(Type t) {
        return getTypeArgument(t, 0);
    }

    static Type getTypeArgument(Type type, int i) {
        return BooleanUtils.defaultIfPredicate(getTypeArguments(type), t -> ObjectUtils.nonNull(t) && t.length > i, t -> t[i]);
    }

    static Type[] getTypeArguments(Type type) {
        return ObjectUtils.defaultIfNull(type, t -> ObjectUtils.defaultIfNull(toParameterizedType(t), ParameterizedType::getActualTypeArguments));
    }

    static ParameterizedType toParameterizedType(Type type) {
        ParameterizedType result = null;
        if (isParameterizedType(type)) {
            result = (ParameterizedType) type;
        } else if (isClass(type)) {
            Class<?> clazz = (Class) type;
            Type genericSuper = clazz.getGenericSuperclass();
            if (ObjectUtils.isNull(genericSuper) || Object.class.equals(genericSuper)) {
                Type[] genericInterfaces = clazz.getGenericInterfaces();
                if (!ArrayUtils.isEmpty(genericInterfaces)) {
                    genericSuper = genericInterfaces[0];
                }
            }
            result = toParameterizedType(genericSuper);
        }
        return result;
    }

    static boolean isUnknown(Type t) {
        return ObjectUtils.isNull(t) || isTypeVariable(t);
    }
}