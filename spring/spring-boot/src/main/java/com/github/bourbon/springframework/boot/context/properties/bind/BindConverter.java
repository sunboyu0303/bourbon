package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.utils.*;
import com.github.bourbon.springframework.boot.convert.ApplicationConversionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.FileEditor;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.*;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.support.GenericConversionService;

import java.beans.PropertyEditor;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 22:14
 */
final class BindConverter {

    private static BindConverter sharedInstance;

    private final List<ConversionService> delegates;

    private BindConverter(List<ConversionService> conversionServices, Consumer<PropertyEditorRegistry> propertyEditorInitializer) {
        List<ConversionService> delegates = ListUtils.newArrayList(new TypeConverterConversionService(propertyEditorInitializer));
        boolean hasApplication = false;
        if (!CollectionUtils.isEmpty(conversionServices)) {
            for (ConversionService conversionService : conversionServices) {
                delegates.add(conversionService);
                hasApplication = hasApplication || conversionService instanceof ApplicationConversionService;
            }
        }
        if (!hasApplication) {
            delegates.add(ApplicationConversionService.getSharedInstance());
        }
        this.delegates = ListUtils.unmodifiableList(delegates);
    }

    boolean canConvert(Object source, ResolvableType targetType, Annotation... targetAnnotations) {
        return canConvert(TypeDescriptor.forObject(source), new ResolvableTypeDescriptor(targetType, targetAnnotations));
    }

    private boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
        for (ConversionService service : delegates) {
            if (service.canConvert(sourceType, targetType)) {
                return true;
            }
        }
        return false;
    }

    <T> T convert(Object source, Bindable<T> target) {
        return convert(source, target.getType(), target.getAnnotations());
    }

    @SuppressWarnings("unchecked")
    <T> T convert(Object source, ResolvableType targetType, Annotation... targetAnnotations) {
        return ObjectUtils.defaultIfNullElseFunction(source, s -> (T) convert(s, TypeDescriptor.forObject(s), new ResolvableTypeDescriptor(targetType, targetAnnotations)));
    }

    private Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        ConversionException failure = null;
        for (ConversionService delegate : delegates) {
            try {
                if (delegate.canConvert(sourceType, targetType)) {
                    return delegate.convert(source, sourceType, targetType);
                }
            } catch (ConversionException ex) {
                if (failure == null && ex instanceof ConversionFailedException) {
                    failure = ex;
                }
            }
        }
        throw ObjectUtils.defaultSupplierIfNullElseFunction(failure, f -> f, () -> new ConverterNotFoundException(sourceType, targetType));
    }

    static BindConverter get(List<ConversionService> services, Consumer<PropertyEditorRegistry> consumer) {
        boolean bool = services == null || (services.size() == 1 && services.get(0) == ApplicationConversionService.getSharedInstance());
        return BooleanUtils.defaultSupplierIfFalse(consumer == null && bool, BindConverter::getSharedInstance, () -> new BindConverter(services, consumer));
    }

    private static BindConverter getSharedInstance() {
        return ObjectUtils.defaultSupplierIfNull(sharedInstance, () -> {
            sharedInstance = new BindConverter(null, null);
            return sharedInstance;
        });
    }

    private static class ResolvableTypeDescriptor extends TypeDescriptor {
        private static final long serialVersionUID = 2047633179615795946L;

        ResolvableTypeDescriptor(ResolvableType resolvableType, Annotation[] annotations) {
            super(resolvableType, null, annotations);
        }
    }

    private static class TypeConverterConversionService extends GenericConversionService {

        TypeConverterConversionService(Consumer<PropertyEditorRegistry> initializer) {
            addConverter(new TypeConverterConverter(initializer));
            ApplicationConversionService.addDelimitedStringConverters(this);
        }

        @Override
        public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (targetType.isArray() && targetType.getElementTypeDescriptor().isPrimitive()) {
                return false;
            }
            return super.canConvert(sourceType, targetType);
        }
    }

    private static class TypeConverterConverter implements ConditionalGenericConverter {

        private static final Set<Class<?>> EXCLUDED_EDITORS = SetUtils.unmodifiableSet(
                SetUtils.newHashSet(CustomNumberEditor.class, CustomBooleanEditor.class, FileEditor.class)
        );

        private final Consumer<PropertyEditorRegistry> initializer;

        private final SimpleTypeConverter matchesOnlyTypeConverter;

        TypeConverterConverter(Consumer<PropertyEditorRegistry> initializer) {
            this.initializer = initializer;
            this.matchesOnlyTypeConverter = createTypeConverter();
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return SetUtils.newHashSet(new ConvertiblePair(String.class, Object.class));
        }

        @Override
        public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
            Class<?> type = targetType.getType();
            if (type == Object.class || Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
                return false;
            }
            PropertyEditor editor = matchesOnlyTypeConverter.getDefaultEditor(type);
            if (ObjectUtils.isNull(editor)) {
                editor = matchesOnlyTypeConverter.findCustomEditor(type, null);
            }
            if (ObjectUtils.isNull(editor) && String.class != type) {
                editor = BeanUtils.findEditorByConvention(type);
            }
            return ObjectUtils.nonNull(editor) && !EXCLUDED_EDITORS.contains(editor.getClass());
        }

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            return createTypeConverter().convertIfNecessary(source, targetType.getType());
        }

        private SimpleTypeConverter createTypeConverter() {
            SimpleTypeConverter typeConverter = new SimpleTypeConverter();
            if (initializer != null) {
                initializer.accept(typeConverter);
            }
            return typeConverter;
        }
    }
}