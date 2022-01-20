package com.github.bourbon.springframework.beans.factory.config;

import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.springframework.beans.factory.HierarchicalBeanFactory;
import com.github.bourbon.springframework.core.convert.ConversionService;
import com.github.bourbon.springframework.utils.StringValueResolver;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 17:59
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void destroySingletons();

    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    String resolveEmbeddedValue(String value);

    void setConversionService(ConversionService conversionService);

    ConversionService getConversionService();

    enum Scope {
        SINGLETON, PROTOTYPE;

        public static Scope getScope(String scope) {
            if (CharSequenceUtils.isEmpty(scope)) {
                return null;
            }
            if (SINGLETON.name().equalsIgnoreCase(scope)) {
                return SINGLETON;
            }
            if (PROTOTYPE.name().equalsIgnoreCase(scope)) {
                return PROTOTYPE;
            }
            return null;
        }
    }
}