package com.github.bourbon.bytecode.bytebuddy.support;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.bytecode.bytebuddy.constant.ByteBuddyConstants;
import com.github.bourbon.bytecode.core.utils.ClassUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 23:10
 */
public class Wrapped<T> {

    private static final AtomicLong ATOMIC_LONG = new AtomicLong();
    private final T object;

    public Wrapped(Class<T> clazz) throws Exception {
        this(clazz, DefaultMonitor.class);
    }

    public Wrapped(Class<T> clazz, Class<?> monitorClazz) throws Exception {
        String proxyClassName = clazz.getName() + ByteBuddyConstants.DOLLARS_PROXY + StringConstants.DOLLARS + ATOMIC_LONG.getAndIncrement();

        Method[] methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> ClassUtils.exceptMethod(m.getName()) && Modifier.isPublic(m.getModifiers())).toArray(Method[]::new);

        DynamicType.Unloaded<?> dynamicType = new ByteBuddy().subclass(clazz).name(proxyClassName)
                .method(ElementMatchers.anyOf(methods)).intercept(MethodDelegation.to(monitorClazz)).make();

        ClassUtils.outputClazz(proxyClassName.replace(CharConstants.DOT, CharConstants.SLASH) + ByteBuddyConstants.DOLLARS_BYTE_BUDDY, dynamicType.getBytes());
        this.object = (T) dynamicType.load(Wrapped.class.getClassLoader()).getLoaded().newInstance();
    }

    public T getObject() {
        return object;
    }
}