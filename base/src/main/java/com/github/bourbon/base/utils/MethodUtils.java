package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/3 14:09
 */
public final class MethodUtils {

    private MethodUtils() {
    }

    private static final Map<Method, String> methodNameMap = new ConcurrentHashMap<>();
    private static final Map<Method, String> shortMethodNameMap = new ConcurrentHashMap<>();

    public static String resolveMethodName(Method method) {
        return resolveMethodName(method, false);
    }

    public static String resolveMethodName(Method method, boolean isShort) {
        Assert.notNull(method, "Null method");
        Map<Method, String> map = isShort ? shortMethodNameMap : methodNameMap;
        return map.computeIfAbsent(method, o -> {
            StringBuilder sb = new StringBuilder();
            Class<?> declaringClass = method.getDeclaringClass();
            sb.append(isShort ? ClassUtils.getShortClassName(declaringClass) : declaringClass.getName());
            sb.append(StringConstants.COLON);
            sb.append(method.getName());
            sb.append(StringConstants.LEFT_PARENTHESES);
            int paramPos = 0;
            Class<?>[] params = method.getParameterTypes();
            int len = params.length;
            for (Class<?> clazz : params) {
                sb.append(clazz.getCanonicalName());
                if (++paramPos < len) {
                    sb.append(StringConstants.COMMA);
                }
            }
            sb.append(StringConstants.RIGHT_PARENTHESES);
            return sb.toString();
        });
    }
}