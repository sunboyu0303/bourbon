package com.github.bourbon.springframework.context.bean.circle;

import com.github.bourbon.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/9 14:18
 */
public class HusbandMother implements FactoryBean<IMother> {

    @Override
    public IMother getObject() {
        return (IMother) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IMother.class},
                (proxy, method, args) -> "婚后媳妇妈妈的职责被婆婆代理了！" + method.getName());
    }

    @Override
    public Class<?> getObjectType() {
        return IMother.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}