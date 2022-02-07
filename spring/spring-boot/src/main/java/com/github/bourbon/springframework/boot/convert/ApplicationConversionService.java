package com.github.bourbon.springframework.boot.convert;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.*;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.StringValueResolver;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 22:30
 */
public class ApplicationConversionService extends FormattingConversionService {

    private static final ApplicationConversionService sharedInstance = new ApplicationConversionService(null, true);

    private final boolean unmodifiable;

    public ApplicationConversionService() {
        this(null);
    }

    public ApplicationConversionService(StringValueResolver embeddedValueResolver) {
        this(embeddedValueResolver, false);
    }

    private ApplicationConversionService(StringValueResolver embeddedValueResolver, boolean unmodifiable) {
        if (embeddedValueResolver != null) {
            setEmbeddedValueResolver(embeddedValueResolver);
        }
        configure(this);
        this.unmodifiable = unmodifiable;
    }

    @Override
    public void addPrinter(Printer<?> printer) {
        assertModifiable();
        super.addPrinter(printer);
    }

    @Override
    public void addParser(Parser<?> parser) {
        assertModifiable();
        super.addParser(parser);
    }

    @Override
    public void addFormatter(Formatter<?> formatter) {
        assertModifiable();
        super.addFormatter(formatter);
    }

    @Override
    public void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter) {
        assertModifiable();
        super.addFormatterForFieldType(fieldType, formatter);
    }

    @Override
    public void addConverter(Converter<?, ?> converter) {
        assertModifiable();
        super.addConverter(converter);
    }

    @Override
    public void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser) {
        assertModifiable();
        super.addFormatterForFieldType(fieldType, printer, parser);
    }

    @Override
    public void addFormatterForFieldAnnotation(AnnotationFormatterFactory<? extends Annotation> annotationFormatterFactory) {
        assertModifiable();
        super.addFormatterForFieldAnnotation(annotationFormatterFactory);
    }

    @Override
    public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
        assertModifiable();
        super.addConverter(sourceType, targetType, converter);
    }

    @Override
    public void addConverter(GenericConverter converter) {
        assertModifiable();
        super.addConverter(converter);
    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> factory) {
        assertModifiable();
        super.addConverterFactory(factory);
    }

    @Override
    public void removeConvertible(Class<?> sourceType, Class<?> targetType) {
        assertModifiable();
        super.removeConvertible(sourceType, targetType);
    }

    private void assertModifiable() {
        if (unmodifiable) {
            throw new UnsupportedOperationException("This ApplicationConversionService cannot be modified");
        }
    }

    public boolean isConvertViaObjectSourceType(TypeDescriptor sourceType, TypeDescriptor targetType) {
        Set<GenericConverter.ConvertiblePair> pairs = ObjectUtils.defaultIfNullElseFunction(getConverter(sourceType, targetType), GenericConverter::getConvertibleTypes);
        if (pairs != null) {
            for (GenericConverter.ConvertiblePair pair : pairs) {
                if (Object.class.equals(pair.getSourceType())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ConversionService getSharedInstance() {
        return sharedInstance;
    }

    public static void configure(FormatterRegistry registry) {
        DefaultConversionService.addDefaultConverters(registry);
        DefaultFormattingConversionService.addDefaultFormatters(registry);
        addApplicationFormatters(registry);
        addApplicationConverters(registry);
    }

    public static void addApplicationConverters(ConverterRegistry registry) {
        addDelimitedStringConverters(registry);
        registry.addConverter(new StringToDurationConverter());
        registry.addConverter(new DurationToStringConverter());
        registry.addConverter(new NumberToDurationConverter());
        registry.addConverter(new DurationToNumberConverter());
        registry.addConverter(new StringToPeriodConverter());
        registry.addConverter(new PeriodToStringConverter());
        registry.addConverter(new NumberToPeriodConverter());
        registry.addConverter(new StringToDataSizeConverter());
        registry.addConverter(new NumberToDataSizeConverter());
        registry.addConverter(new StringToFileConverter());
        registry.addConverter(new InputStreamSourceToByteArrayConverter());
        registry.addConverterFactory(new LenientStringToEnumConverterFactory());
        registry.addConverterFactory(new LenientBooleanToEnumConverterFactory());
        if (registry instanceof ConversionService) {
            addApplicationConverters(registry, (ConversionService) registry);
        }
    }

    private static void addApplicationConverters(ConverterRegistry registry, ConversionService conversionService) {
        registry.addConverter(new CharSequenceToObjectConverter(conversionService));
    }

    public static void addDelimitedStringConverters(ConverterRegistry registry) {
        ConversionService service = (ConversionService) registry;
        registry.addConverter(new ArrayToDelimitedStringConverter(service));
        registry.addConverter(new CollectionToDelimitedStringConverter(service));
        registry.addConverter(new DelimitedStringToArrayConverter(service));
        registry.addConverter(new DelimitedStringToCollectionConverter(service));
    }

    public static void addApplicationFormatters(FormatterRegistry registry) {
        registry.addFormatter(new CharArrayFormatter());
        registry.addFormatter(new InetAddressFormatter());
        registry.addFormatter(new IsoOffsetFormatter());
    }

    public static void addBeans(FormatterRegistry registry, ListableBeanFactory beanFactory) {
        Set<Object> beans = SetUtils.newLinkedHashSet();
        beans.addAll(beanFactory.getBeansOfType(GenericConverter.class).values());
        beans.addAll(beanFactory.getBeansOfType(Converter.class).values());
        beans.addAll(beanFactory.getBeansOfType(Printer.class).values());
        beans.addAll(beanFactory.getBeansOfType(Parser.class).values());
        for (Object bean : beans) {
            if (bean instanceof GenericConverter) {
                registry.addConverter((GenericConverter) bean);
            } else if (bean instanceof Converter) {
                registry.addConverter((Converter<?, ?>) bean);
            } else if (bean instanceof Formatter) {
                registry.addFormatter((Formatter<?>) bean);
            } else if (bean instanceof Printer) {
                registry.addPrinter((Printer<?>) bean);
            } else if (bean instanceof Parser) {
                registry.addParser((Parser<?>) bean);
            }
        }
    }
}