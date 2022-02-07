package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.ArrayUtils;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import kotlin.reflect.KFunction;
import kotlin.reflect.KParameter;
import kotlin.reflect.jvm.ReflectJvmMapping;
import org.springframework.beans.BeanUtils;
import org.springframework.core.*;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.convert.ConversionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/24 18:00
 */
class ValueObjectBinder implements DataObjectBinder {

    private final BindConstructorProvider constructorProvider;

    ValueObjectBinder(BindConstructorProvider constructorProvider) {
        this.constructorProvider = constructorProvider;
    }

    @Override
    public <T> T bind(ConfigurationPropertyName name, Bindable<T> target, Binder.Context context, DataObjectPropertyBinder propertyBinder) {
        ValueObject<T> valueObject = ValueObject.get(target, constructorProvider, context);
        if (valueObject == null) {
            return null;
        }
        context.pushConstructorBoundTypes(target.getType().resolve());
        List<ConstructorParameter> parameters = valueObject.getConstructorParameters();
        List<Object> args = ListUtils.newArrayList(parameters.size());
        boolean bound = false;
        for (ConstructorParameter parameter : parameters) {
            Object arg = parameter.bind(propertyBinder);
            bound = bound || arg != null;
            args.add(ObjectUtils.defaultSupplierIfNull(arg, () -> getDefaultValue(context, parameter)));
        }
        context.clearConfigurationProperty();
        context.popConstructorBoundTypes();
        return BooleanUtils.defaultIfFalse(bound, () -> valueObject.instantiate(args), null);
    }

    @Override
    public <T> T create(Bindable<T> target, Binder.Context context) {
        return ObjectUtils.defaultIfNullElseFunction(ValueObject.get(target, constructorProvider, context), v -> v.instantiate(
                v.getConstructorParameters().stream().map(p -> getDefaultValue(context, p)).collect(Collectors.toList())
        ));
    }

    private <T> T getDefaultValue(Binder.Context context, ConstructorParameter parameter) {
        ResolvableType type = parameter.getType();
        Annotation[] annotations = parameter.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof DefaultValue) {
                return BooleanUtils.defaultSupplierIfPredicate(((DefaultValue) annotation).value(), ArrayUtils::isNotEmpty, v -> convertDefaultValue(context.getConverter(), v, type, annotations), () -> getNewInstanceIfPossible(context, type));
            }
        }
        return null;
    }

    private <T> T convertDefaultValue(BindConverter converter, String[] defaultValue, ResolvableType type, Annotation[] annotations) {
        try {
            return converter.convert(defaultValue, type, annotations);
        } catch (ConversionException ex) {
            if (defaultValue.length == 1) {
                return converter.convert(defaultValue[0], type, annotations);
            }
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getNewInstanceIfPossible(Binder.Context context, ResolvableType type) {
        Class<T> resolved = (Class<T>) type.resolve();
        Assert.isTrue(resolved == null || isEmptyDefaultValueAllowed(resolved), () -> "Parameter of type " + type + " must have a non-empty default value.");
        return ObjectUtils.defaultSupplierIfNull(create(Bindable.of(type), context), () -> ObjectUtils.defaultIfNullElseFunction(resolved, BeanUtils::instantiateClass));
    }

    private boolean isEmptyDefaultValueAllowed(Class<?> type) {
        return !type.isPrimitive() && !type.isEnum() && !isAggregate(type) && !type.getName().startsWith("java.lang");
    }

    private boolean isAggregate(Class<?> type) {
        return type.isArray() || Map.class.isAssignableFrom(type) || Collection.class.isAssignableFrom(type);
    }

    private abstract static class ValueObject<T> {

        private final Constructor<T> constructor;

        protected ValueObject(Constructor<T> constructor) {
            this.constructor = constructor;
        }

        T instantiate(List<Object> args) {
            return BeanUtils.instantiateClass(constructor, args.toArray());
        }

        abstract List<ConstructorParameter> getConstructorParameters();

        @SuppressWarnings("unchecked")
        static <T> ValueObject<T> get(Bindable<T> bindable, BindConstructorProvider provider, Binder.Context context) {
            Class<T> type = (Class<T>) bindable.getType().resolve();
            if (type == null || type.isEnum() || Modifier.isAbstract(type.getModifiers())) {
                return null;
            }
            return ObjectUtils.defaultIfNullElseFunction(provider.getBindConstructor(bindable, context.isNestedConstructorBinding()), c -> BooleanUtils.defaultSupplierIfFalse(
                    KotlinDetector.isKotlinType(type), () -> KotlinValueObject.get((Constructor<T>) c, bindable.getType()), () -> DefaultValueObject.get(c, bindable.getType())
            ));
        }
    }

    private static final class KotlinValueObject<T> extends ValueObject<T> {

        private static final Annotation[] ANNOTATION_ARRAY = new Annotation[0];

        private final List<ConstructorParameter> constructorParameters;

        private KotlinValueObject(Constructor<T> primaryConstructor, KFunction<T> kotlinConstructor, ResolvableType type) {
            super(primaryConstructor);
            constructorParameters = parseConstructorParameters(kotlinConstructor, type);
        }

        private List<ConstructorParameter> parseConstructorParameters(KFunction<T> kotlinConstructor, ResolvableType type) {
            return ListUtils.unmodifiableList(kotlinConstructor.getParameters().stream().map(
                    p -> new ConstructorParameter(getParameterName(p), ResolvableType.forType(ReflectJvmMapping.getJavaType(p.getType()), type), p.getAnnotations().toArray(ANNOTATION_ARRAY))
            ).collect(Collectors.toList()));
        }

        private String getParameterName(KParameter parameter) {
            return MergedAnnotations.from(parameter, parameter.getAnnotations().toArray(ANNOTATION_ARRAY)).get(Name.class).getValue(MergedAnnotation.VALUE, String.class).orElseGet(parameter::getName);
        }

        @Override
        List<ConstructorParameter> getConstructorParameters() {
            return constructorParameters;
        }

        static <T> ValueObject<T> get(Constructor<T> bindConstructor, ResolvableType type) {
            return ObjectUtils.defaultSupplierIfNullElseFunction(ReflectJvmMapping.getKotlinFunction(bindConstructor), c -> new KotlinValueObject<>(bindConstructor, c, type), () -> DefaultValueObject.get(bindConstructor, type));
        }
    }

    private static final class DefaultValueObject<T> extends ValueObject<T> {

        private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

        private final List<ConstructorParameter> constructorParameters;

        private DefaultValueObject(Constructor<T> constructor, ResolvableType type) {
            super(constructor);
            this.constructorParameters = parseConstructorParameters(constructor, type);
        }

        private static List<ConstructorParameter> parseConstructorParameters(Constructor<?> constructor, ResolvableType type) {
            String[] names = PARAMETER_NAME_DISCOVERER.getParameterNames(constructor);
            Assert.notNull(names, () -> "Failed to extract parameter names for " + constructor);
            Parameter[] parameters = constructor.getParameters();
            List<ConstructorParameter> result = ListUtils.newArrayList(parameters.length);
            for (int i = 0; i < parameters.length; i++) {
                String name = MergedAnnotations.from(parameters[i]).get(Name.class).getValue(MergedAnnotation.VALUE, String.class).orElse(names[i]);
                ResolvableType parameterType = ResolvableType.forMethodParameter(new MethodParameter(constructor, i), type);
                result.add(new ConstructorParameter(name, parameterType, parameters[i].getDeclaredAnnotations()));
            }
            return ListUtils.unmodifiableList(result);
        }

        @Override
        List<ConstructorParameter> getConstructorParameters() {
            return constructorParameters;
        }

        @SuppressWarnings("unchecked")
        static <T> ValueObject<T> get(Constructor<?> bindConstructor, ResolvableType type) {
            return new DefaultValueObject<>((Constructor<T>) bindConstructor, type);
        }
    }

    private static class ConstructorParameter {

        private final String name;

        private final ResolvableType type;

        private final Annotation[] annotations;

        ConstructorParameter(String name, ResolvableType type, Annotation[] annotations) {
            this.name = DataObjectPropertyName.toDashedForm(name);
            this.type = type;
            this.annotations = annotations;
        }

        Object bind(DataObjectPropertyBinder binder) {
            return binder.bindProperty(name, Bindable.of(type).withAnnotations(annotations));
        }

        Annotation[] getAnnotations() {
            return annotations;
        }

        ResolvableType getType() {
            return type;
        }
    }
}