package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertySource;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyState;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 22:40
 */
class JavaBeanBinder implements DataObjectBinder {

    static final JavaBeanBinder INSTANCE = new JavaBeanBinder();

    @Override
    public <T> T bind(ConfigurationPropertyName name, Bindable<T> target, Binder.Context context, DataObjectPropertyBinder propertyBinder) {
        return ObjectUtils.defaultIfNullElseFunction(Bean.get(target, target.getValue() != null && hasKnownBindableProperties(name, context)), bean -> {
            BeanSupplier<T> beanSupplier = bean.getSupplier(target);
            return BooleanUtils.defaultIfFalse(bind(propertyBinder, bean, beanSupplier, context), beanSupplier);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T create(Bindable<T> target, Binder.Context context) {
        return ObjectUtils.defaultIfNullElseFunction((Class<T>) target.getType().resolve(), BeanUtils::instantiateClass);
    }

    private boolean hasKnownBindableProperties(ConfigurationPropertyName name, Binder.Context context) {
        for (ConfigurationPropertySource source : context.getSources()) {
            if (source.containsDescendantOf(name) == ConfigurationPropertyState.PRESENT) {
                return true;
            }
        }
        return false;
    }

    private <T> boolean bind(DataObjectPropertyBinder propertyBinder, Bean<T> bean, BeanSupplier<T> beanSupplier, Binder.Context context) {
        boolean bound = false;
        for (BeanProperty beanProperty : bean.getProperties().values()) {
            bound |= bind(beanSupplier, propertyBinder, beanProperty);
            context.clearConfigurationProperty();
        }
        return bound;
    }

    private <T> boolean bind(BeanSupplier<T> beanSupplier, DataObjectPropertyBinder propertyBinder, BeanProperty property) {
        String propertyName = property.getName();
        ResolvableType type = property.getType();
        Supplier<Object> value = property.getValue(beanSupplier);
        Annotation[] annotations = property.getAnnotations();
        return ObjectUtils.defaultIfNullElseFunction(propertyBinder.bindProperty(propertyName, Bindable.of(type).withSuppliedValue(value).withAnnotations(annotations)), bound -> {
            if (property.isSettable()) {
                property.setValue(beanSupplier, bound);
            } else if (value == null || !bound.equals(value.get())) {
                throw new IllegalStateException("No setter found for property: " + property.getName());
            }
            return true;
        }, false);
    }

    static class Bean<T> {

        private static Bean<?> cached;

        private final ResolvableType type;

        private final Class<?> resolvedType;

        private final Map<String, BeanProperty> properties = new LinkedHashMap<>();

        Bean(ResolvableType type, Class<?> resolvedType) {
            this.type = type;
            this.resolvedType = resolvedType;
            addProperties(resolvedType);
        }

        private void addProperties(Class<?> type) {
            while (type != null && !Object.class.equals(type)) {
                addProperties(getSorted(type, Class::getDeclaredMethods, Method::getName), getSorted(type, Class::getDeclaredFields, Field::getName));
                type = type.getSuperclass();
            }
        }

        private <S, E> E[] getSorted(S source, Function<S, E[]> elements, Function<E, String> name) {
            E[] result = elements.apply(source);
            Arrays.sort(result, Comparator.comparing(name));
            return result;
        }

        protected void addProperties(Method[] declaredMethods, Field[] declaredFields) {
            for (int i = 0; i < declaredMethods.length; i++) {
                if (!isCandidate(declaredMethods[i])) {
                    declaredMethods[i] = null;
                }
            }
            for (Method method : declaredMethods) {
                addMethodIfPossible(method, "is", 0, BeanProperty::addGetter);
            }
            for (Method method : declaredMethods) {
                addMethodIfPossible(method, "get", 0, BeanProperty::addGetter);
            }
            for (Method method : declaredMethods) {
                addMethodIfPossible(method, "set", 1, BeanProperty::addSetter);
            }
            for (Field field : declaredFields) {
                addField(field);
            }
        }

        private boolean isCandidate(Method method) {
            int modifiers = method.getModifiers();
            return !Modifier.isPrivate(modifiers) && !Modifier.isProtected(modifiers) && !Modifier.isAbstract(modifiers) && !Modifier.isStatic(modifiers) &&
                    !Object.class.equals(method.getDeclaringClass()) && !Class.class.equals(method.getDeclaringClass()) && method.getName().indexOf(CharConstants.DOLLAR) == -1;
        }

        private void addMethodIfPossible(Method method, String prefix, int parameterCount, BiConsumer<BeanProperty, Method> consumer) {
            if (method != null && method.getParameterCount() == parameterCount && method.getName().startsWith(prefix) && method.getName().length() > prefix.length()) {
                consumer.accept(properties.computeIfAbsent(Introspector.decapitalize(method.getName().substring(prefix.length())), this::getBeanProperty), method);
            }
        }

        private BeanProperty getBeanProperty(String name) {
            return new BeanProperty(name, type);
        }

        private void addField(Field field) {
            BeanProperty property = properties.get(field.getName());
            if (property != null) {
                property.addField(field);
            }
        }

        Map<String, BeanProperty> getProperties() {
            return properties;
        }

        @SuppressWarnings("unchecked")
        BeanSupplier<T> getSupplier(Bindable<T> target) {
            return new BeanSupplier<>(() -> {
                T instance = null;
                if (target.getValue() != null) {
                    instance = target.getValue().get();
                }
                if (instance == null) {
                    instance = (T) BeanUtils.instantiateClass(resolvedType);
                }
                return instance;
            });
        }

        @SuppressWarnings("unchecked")
        static <T> Bean<T> get(Bindable<T> bindable, boolean canCallGetValue) {
            ResolvableType type = bindable.getType();
            Class<?> resolvedType = type.resolve(Object.class);
            Supplier<T> value = bindable.getValue();
            T instance = null;
            if (canCallGetValue && value != null) {
                instance = value.get();
                resolvedType = ObjectUtils.defaultIfNullElseFunction(instance, T::getClass, resolvedType);
            }
            if (instance == null && !isInstantiable(resolvedType)) {
                return null;
            }
            Bean<?> bean = Bean.cached;
            if (bean == null || !bean.isOfType(type, resolvedType)) {
                bean = new Bean<>(type, resolvedType);
                cached = bean;
            }
            return (Bean<T>) bean;
        }

        private static boolean isInstantiable(Class<?> type) {
            if (type.isInterface()) {
                return false;
            }
            try {
                type.getDeclaredConstructor();
                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        private boolean isOfType(ResolvableType type, Class<?> resolvedType) {
            return BooleanUtils.defaultSupplierIfFalse(this.type.hasGenerics() || type.hasGenerics(), () -> this.type.equals(type), () -> this.resolvedType != null && this.resolvedType.equals(resolvedType));
        }
    }

    private static class BeanSupplier<T> implements Supplier<T> {

        private final Supplier<T> factory;

        private T instance;

        BeanSupplier(Supplier<T> factory) {
            this.factory = factory;
        }

        @Override
        public T get() {
            return ObjectUtils.defaultSupplierIfNull(instance, () -> {
                instance = factory.get();
                return instance;
            });
        }
    }

    static class BeanProperty {

        private final String name;

        private final ResolvableType declaringClassType;

        private Method getter;

        private Method setter;

        private Field field;

        BeanProperty(String name, ResolvableType declaringClassType) {
            this.name = DataObjectPropertyName.toDashedForm(name);
            this.declaringClassType = declaringClassType;
        }

        void addGetter(Method getter) {
            if (this.getter == null || isBetterGetter(getter)) {
                this.getter = getter;
            }
        }

        private boolean isBetterGetter(Method getter) {
            return this.getter != null && this.getter.getName().startsWith("is");
        }

        void addSetter(Method setter) {
            if (this.setter == null || isBetterSetter(setter)) {
                this.setter = setter;
            }
        }

        private boolean isBetterSetter(Method setter) {
            return getter != null && getter.getReturnType().equals(setter.getParameterTypes()[0]);
        }

        void addField(Field field) {
            if (this.field == null) {
                this.field = field;
            }
        }

        String getName() {
            return name;
        }

        ResolvableType getType() {
            return ObjectUtils.defaultSupplierIfNullElseFunction(setter,
                    s -> ResolvableType.forMethodParameter(new MethodParameter(s, 0), declaringClassType),
                    () -> ResolvableType.forMethodParameter(new MethodParameter(getter, -1), declaringClassType)
            );
        }

        Annotation[] getAnnotations() {
            try {
                return ObjectUtils.defaultIfNullElseFunction(field, Field::getDeclaredAnnotations);
            } catch (Exception ex) {
                return null;
            }
        }

        Supplier<Object> getValue(Supplier<?> instance) {
            return ObjectUtils.defaultIfNullElseFunction(getter, g -> () -> {
                try {
                    ReflectionUtils.makeAccessible(g);
                    return g.invoke(instance.get());
                } catch (Exception ex) {
                    throw new IllegalStateException("Unable to get value for property " + name, ex);
                }
            });
        }

        boolean isSettable() {
            return setter != null;
        }

        void setValue(Supplier<?> instance, Object value) {
            try {
                ReflectionUtils.makeAccessible(setter);
                setter.invoke(instance.get(), value);
            } catch (Exception ex) {
                throw new IllegalStateException("Unable to set value for property " + name, ex);
            }
        }
    }
}