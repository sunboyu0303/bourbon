package com.github.bourbon.common.instantiate.support;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.common.instantiate.InstantiationStrategy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 01:08
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(Class<?> clazz, Constructor<?> ctor, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return super.equals(obj);
            }
        });
        return ObjectUtils.defaultSupplierIfNullElseFunction(ctor, c -> enhancer.create(c.getParameterTypes(), args), enhancer::create);
    }
}