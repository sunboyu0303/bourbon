package com.github.bourbon.springframework.aop.support;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 10:25
 */
public final class AopUtils extends org.springframework.aop.support.AopUtils {

    public static Object getTarget(Object o) {
        return BooleanUtils.defaultIfFalse(isAopProxy(o), () -> {
            Object target = o;
            do {
                if (isJdkDynamicProxy(target)) {
                    target = getJdkDynamicProxyTargetObject(target);
                } else {
                    target = getCglibProxyTargetObject(target);
                }
            } while (isAopProxy(target));
            return target;
        }, o);
    }

    private static Object getCglibProxyTargetObject(Object o) {
        return ObjectUtils.defaultIfNull(getField(o.getClass(), "CGLIB$CALLBACK_0"), h -> ObjectUtils.defaultIfNull(
                getFieldTarget(o, h), i -> ObjectUtils.defaultIfNull(getField(i.getClass(), "advised"),
                        a -> ObjectUtils.defaultIfNull((AdvisedSupport) getFieldTarget(i, a), s -> {
                            try {
                                return s.getTargetSource().getTarget();
                            } catch (Exception e) {
                                return null;
                            }
                        })
                )
        ));
    }

    private static Object getJdkDynamicProxyTargetObject(Object o) {
        return ObjectUtils.defaultIfNull(getField(o.getClass().getSuperclass(), StringConstants.SMALL_H), h -> ObjectUtils.defaultIfNull(
                getFieldTarget(o, h), aop -> ObjectUtils.defaultIfNull(getField(aop.getClass(), "advised"),
                        a -> ObjectUtils.defaultIfNull((AdvisedSupport) getFieldTarget(aop, a), s -> {
                            try {
                                return s.getTargetSource().getTarget();
                            } catch (Exception e) {
                                return null;
                            }
                        })
                )
        ));
    }

    private static Object getFieldTarget(Object object, Field field) {
        try {
            ReflectionUtils.makeAccessible(field);
            return field.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private AopUtils() {
    }
}