package com.github.bourbon.springframework.context.support;

import com.github.bourbon.base.convert.Converter;
import com.github.bourbon.springframework.beans.factory.FactoryBean;
import com.github.bourbon.springframework.beans.factory.InitializingBean;
import com.github.bourbon.springframework.core.convert.ConversionService;
import com.github.bourbon.springframework.core.convert.converter.ConverterFactory;
import com.github.bourbon.springframework.core.convert.converter.ConverterRegistry;
import com.github.bourbon.springframework.core.convert.converter.GenericConverter;
import com.github.bourbon.springframework.core.convert.support.DefaultConversionService;
import com.github.bourbon.springframework.core.convert.support.GenericConversionService;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 13:56
 */
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    private Set<?> converters;

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }

    private GenericConversionService conversionService = new DefaultConversionService();

    @Override
    public ConversionService getObject() {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        registerConverters(converters, conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegistry registry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                    registry.addConverter((GenericConverter) converter);
                } else if (converter instanceof Converter<?, ?>) {
                    registry.addConverter((Converter<?, ?>) converter);
                } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
                } else {
                    throw new IllegalArgumentException("Each converter object must implement one of the Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }
}