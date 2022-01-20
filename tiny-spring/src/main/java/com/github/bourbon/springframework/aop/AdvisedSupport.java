package com.github.bourbon.springframework.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 15:36
 */
public class AdvisedSupport {

    private TargetSource targetSource;

    private MethodInterceptor methodInterceptor;

    private MethodMatcher methodMatcher;

    private boolean proxyTargetClass;

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public AdvisedSupport setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
        return this;
    }

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    public AdvisedSupport setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
        return this;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public AdvisedSupport setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
        return this;
    }

    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public AdvisedSupport setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
        return this;
    }
}