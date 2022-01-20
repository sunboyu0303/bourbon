package com.github.bourbon.springframework.beans.factory.config;

import com.github.bourbon.springframework.beans.PropertyValues;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 10:15
 */
public class BeanDefinition {

    private final Class<?> clazz;
    private final PropertyValues propertyValues;

    private String initMethodName;
    private String destroyMethodName;

    private ConfigurableBeanFactory.Scope scope = ConfigurableBeanFactory.Scope.SINGLETON;
    private boolean singleton = true;
    private boolean prototype = false;

    private BeanDefinition(Class<?> clazz) {
        this(clazz, null);
    }

    private BeanDefinition(Class<?> clazz, PropertyValues propertyValues) {
        this.clazz = clazz;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public BeanDefinition setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
        return this;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public BeanDefinition setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
        return this;
    }

    public ConfigurableBeanFactory.Scope getScope() {
        return scope;
    }

    public BeanDefinition setScope(ConfigurableBeanFactory.Scope scope) {
        this.scope = scope;
        this.singleton = ConfigurableBeanFactory.Scope.SINGLETON == scope;
        this.prototype = ConfigurableBeanFactory.Scope.PROTOTYPE == scope;
        return this;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public static BeanDefinition of(Class<?> c) {
        return new BeanDefinition(c);
    }

    public static BeanDefinition of(Class<?> c, PropertyValues pvs) {
        return new BeanDefinition(c, pvs);
    }
}