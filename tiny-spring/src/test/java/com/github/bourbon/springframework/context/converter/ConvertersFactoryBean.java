package com.github.bourbon.springframework.context.converter;

import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.springframework.beans.factory.FactoryBean;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 15:30
 */
public class ConvertersFactoryBean implements FactoryBean<Set<?>> {

    @Override
    public Set<?> getObject() {
        return SetUtils.newHashSet(new StringToLocalDateConverter("yyyy-MM-dd"));
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}