package com.github.bourbon.springframework.beans.factory.support;

import com.github.bourbon.base.utils.ClassLoaderUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.FactoryBean;
import com.github.bourbon.springframework.beans.factory.config.BeanDefinition;
import com.github.bourbon.springframework.beans.factory.config.BeanPostProcessor;
import com.github.bourbon.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.github.bourbon.springframework.core.convert.ConversionService;
import com.github.bourbon.springframework.utils.StringValueResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 21:52
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    protected List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    private final ClassLoader beanClassLoader = ClassLoaderUtils.getClassLoader();

    protected ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    private final List<StringValueResolver> embeddedValueResolvers = ListUtils.newArrayList();

    private ConversionService conversionService;

    @Override
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ConversionService getConversionService() {
        return conversionService;
    }

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName, null);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return doGetBean(beanName, args);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> type) {
        return (T) getBean(beanName);
    }

    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }

    protected abstract boolean containsBeanDefinition(String beanName);

    protected Object doGetBean(final String beanName, final Object[] args) {
        return ObjectUtils.defaultSupplierIfNull(getSingleton(beanName), sharedInstance -> getObjectForBeanInstance(sharedInstance, beanName), () -> getObjectForBeanInstance(createBean(beanName, getBeanDefinition(beanName), args), beanName));
    }

    private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }
        Object object = getCachedObjectForFactoryBean(beanName);
        if (object == null) {
            object = getObjectFromFactoryBean((FactoryBean<?>) beanInstance, beanName);
        }
        return object;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }
}