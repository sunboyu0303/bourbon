package com.github.bourbon.springframework.aop.framework;

import com.github.bourbon.springframework.aop.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 15:42
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), advised.getTargetSource().getTargetClass(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = advised.getTargetSource().getTarget();
        if (advised.getMethodMatcher().matches(method, target.getClass())) {
            return advised.getMethodInterceptor().invoke(new ReflectiveMethodInvocation(target, method, args));
        }
        return method.invoke(target, args);
    }
}