package com.github.bourbon.springframework.beans.factory.config;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 15:15
 */
public class BeanReference {

    private final String beanName;

    private BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public static BeanReference of(String beanName) {
        return new BeanReference(beanName);
    }
}