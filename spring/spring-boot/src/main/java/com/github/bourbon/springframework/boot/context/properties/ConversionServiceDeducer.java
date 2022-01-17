package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.springframework.boot.convert.ApplicationConversionService;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 19:57
 */
class ConversionServiceDeducer {

    private final ApplicationContext applicationContext;

    ConversionServiceDeducer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    List<ConversionService> getConversionServices() {
        return BooleanUtils.defaultSupplierIfFalse(hasUserDefinedConfigurationServiceBean(),
                () -> ListUtils.newArrayList(applicationContext.getBean(ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)),
                () -> BooleanUtils.defaultIfAssignableFrom(applicationContext, ConfigurableApplicationContext.class, ac -> getConversionServices((ConfigurableApplicationContext) ac))
        );
    }

    private List<ConversionService> getConversionServices(ConfigurableApplicationContext applicationContext) {
        List<ConversionService> conversionServices = ListUtils.newArrayList();
        if (applicationContext.getBeanFactory().getConversionService() != null) {
            conversionServices.add(applicationContext.getBeanFactory().getConversionService());
        }
        ConverterBeans converterBeans = new ConverterBeans(applicationContext);
        if (!converterBeans.isEmpty()) {
            ApplicationConversionService beansConverterService = new ApplicationConversionService();
            converterBeans.addTo(beansConverterService);
            conversionServices.add(beansConverterService);
        }
        return conversionServices;
    }

    private boolean hasUserDefinedConfigurationServiceBean() {
        String beanName = ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME;
        return applicationContext.containsBean(beanName) && applicationContext.getAutowireCapableBeanFactory().isTypeMatch(beanName, ConversionService.class);
    }

    private static class ConverterBeans {

        @SuppressWarnings("rawtypes")
        private final List<Converter> converters;

        private final List<GenericConverter> genericConverters;

        @SuppressWarnings("rawtypes")
        private final List<Formatter> formatters;

        ConverterBeans(ConfigurableApplicationContext applicationContext) {
            ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
            this.converters = beans(Converter.class, ConfigurationPropertiesBinding.VALUE, beanFactory);
            this.genericConverters = beans(GenericConverter.class, ConfigurationPropertiesBinding.VALUE, beanFactory);
            this.formatters = beans(Formatter.class, ConfigurationPropertiesBinding.VALUE, beanFactory);
        }

        private <T> List<T> beans(Class<T> type, String qualifier, ListableBeanFactory beanFactory) {
            return ListUtils.newArrayList(BeanFactoryAnnotationUtils.qualifiedBeansOfType(beanFactory, type, qualifier).values());
        }

        boolean isEmpty() {
            return converters.isEmpty() && genericConverters.isEmpty() && formatters.isEmpty();
        }

        void addTo(FormatterRegistry registry) {
            for (Converter<?, ?> converter : converters) {
                registry.addConverter(converter);
            }
            for (GenericConverter genericConverter : genericConverters) {
                registry.addConverter(genericConverter);
            }
            for (Formatter<?> formatter : formatters) {
                registry.addFormatter(formatter);
            }
        }
    }
}