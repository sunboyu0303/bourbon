package com.github.bourbon.springframework.beans.factory.support;

import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ReflectUtils;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.DisposableBean;
import com.github.bourbon.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 00:30
 */
public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
        if (!CharSequenceUtils.isEmpty(destroyMethodName) && !(bean instanceof DisposableBean && NAME.equals(destroyMethodName))) {
            Method destroyMethod = ReflectUtils.getMethod(bean.getClass(), destroyMethodName);
            if (null == destroyMethod) {
                throw new BeansException("Couldn't find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
            }
            destroyMethod.invoke(bean);
        }
    }
}