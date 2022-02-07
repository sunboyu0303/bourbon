package com.github.bourbon.springframework.core.convert.support;

import com.github.bourbon.base.convert.Converter;
import com.github.bourbon.base.lang.Pair;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.core.convert.ConversionService;
import com.github.bourbon.springframework.core.convert.converter.ConverterFactory;
import com.github.bourbon.springframework.core.convert.converter.ConverterRegistry;
import com.github.bourbon.springframework.core.convert.converter.GenericConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 11:21
 */
public class GenericConversionService implements ConversionService, ConverterRegistry {

    private final Map<Pair<Class<?>, Class<?>>, GenericConverter> converters = new HashMap<>();

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return getConverter(sourceType, targetType) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Object source, Class<T> targetType) {
        Class<?> sourceType = source.getClass();
        return ObjectUtils.defaultIfNullElseFunction(getConverter(sourceType, targetType), c -> (T) c.convert(source, sourceType, targetType));
    }

    @Override
    public void addConverter(Converter<?, ?> converter) {
        ConverterAdapter converterAdapter = new ConverterAdapter(getRequiredTypeInfo(converter), converter);
        converterAdapter.getConvertibleTypes().forEach(convertibleType -> converters.put(convertibleType, converterAdapter));
    }

    @Override
    public void addConverter(GenericConverter converter) {
        converter.getConvertibleTypes().forEach(convertibleType -> converters.put(convertibleType, converter));
    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> converterFactory) {
        ConverterFactoryAdapter converterFactoryAdapter = new ConverterFactoryAdapter(getRequiredTypeInfo(converterFactory), converterFactory);
        converterFactoryAdapter.getConvertibleTypes().forEach(convertibleType -> converters.put(convertibleType, converterFactoryAdapter));
    }

    private GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        List<Class<?>> sourceCandidates = getClassHierarchy(sourceType);
        List<Class<?>> targetCandidates = getClassHierarchy(targetType);
        for (Class<?> sourceCandidate : sourceCandidates) {
            for (Class<?> targetCandidate : targetCandidates) {
                GenericConverter converter = converters.get(Pair.of(sourceCandidate, targetCandidate));
                if (converter != null) {
                    return converter;
                }
            }
        }
        return null;
    }

    private List<Class<?>> getClassHierarchy(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<>();
        while (clazz != null) {
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }

    private Pair<Class<?>, Class<?>> getRequiredTypeInfo(Object object) {
        Type[] actualTypeArguments = ((ParameterizedType) object.getClass().getGenericInterfaces()[0]).getActualTypeArguments();
        return Pair.of((Class<?>) actualTypeArguments[0], (Class<?>) actualTypeArguments[1]);
    }

    private final class ConverterAdapter implements GenericConverter {
        private final Pair<Class<?>, Class<?>> typeInfo;

        private final Converter<Object, Object> converter;

        @SuppressWarnings("unchecked")
        private ConverterAdapter(Pair<Class<?>, Class<?>> typeInfo, Converter<?, ?> converter) {
            this.typeInfo = typeInfo;
            this.converter = (Converter<Object, Object>) converter;
        }

        @Override
        public Set<Pair<Class<?>, Class<?>>> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            return converter.convert(source, null);
        }
    }

    private final class ConverterFactoryAdapter implements GenericConverter {
        private final Pair<Class<?>, Class<?>> typeInfo;

        private final ConverterFactory<Object, Object> converterFactory;

        @SuppressWarnings("unchecked")
        private ConverterFactoryAdapter(Pair<Class<?>, Class<?>> typeInfo, ConverterFactory<?, ?> converterFactory) {
            this.typeInfo = typeInfo;
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
        }

        @Override
        public Set<Pair<Class<?>, Class<?>>> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            return converterFactory.getConverter(targetType).convert(source, null);
        }
    }
}