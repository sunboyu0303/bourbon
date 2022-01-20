package com.github.bourbon.springframework.aop.framework;

import com.github.bourbon.springframework.aop.AdvisedSupport;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 17:20
 */
public class ProxyFactory {

    private final AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    private AopProxy createAopProxy() {
        return advisedSupport.isProxyTargetClass() ? new Cglib2AopProxy(advisedSupport) : new JdkDynamicAopProxy(advisedSupport);
    }
}