package com.github.bourbon.springframework.boot.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Collection;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 18:45
 */
public class TypeExcludeFilter implements TypeFilter, BeanFactoryAware {

    private BeanFactory beanFactory;

    private Collection<TypeExcludeFilter> delegates;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        if (beanFactory instanceof ListableBeanFactory && getClass() == TypeExcludeFilter.class) {
            for (TypeExcludeFilter delegate : getDelegates()) {
                if (delegate.match(metadataReader, metadataReaderFactory)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Collection<TypeExcludeFilter> getDelegates() {
        Collection<TypeExcludeFilter> delegates = this.delegates;
        if (delegates == null) {
            delegates = ((ListableBeanFactory) beanFactory).getBeansOfType(TypeExcludeFilter.class).values();
            this.delegates = delegates;
        }
        return delegates;
    }

    @Override
    public boolean equals(Object obj) {
        throw new IllegalStateException("TypeExcludeFilter " + getClass() + " has not implemented equals");
    }

    @Override
    public int hashCode() {
        throw new IllegalStateException("TypeExcludeFilter " + getClass() + " has not implemented hashCode");
    }
}