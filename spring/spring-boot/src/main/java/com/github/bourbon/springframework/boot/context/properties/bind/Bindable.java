package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.style.ToStringCreator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 15:08
 */
public final class Bindable<T> {

    private static final Annotation[] NO_ANNOTATIONS = {};

    private static final EnumSet<BindRestriction> NO_BIND_RESTRICTIONS = EnumSet.noneOf(BindRestriction.class);

    private final ResolvableType type;

    private final ResolvableType boxedType;

    private final Supplier<T> value;

    private final Annotation[] annotations;

    private final EnumSet<BindRestriction> bindRestrictions;

    private Bindable(ResolvableType type, ResolvableType boxedType, Supplier<T> value, Annotation[] annotations, EnumSet<BindRestriction> bindRestrictions) {
        this.type = type;
        this.boxedType = boxedType;
        this.value = value;
        this.annotations = annotations;
        this.bindRestrictions = bindRestrictions;
    }

    public ResolvableType getType() {
        return type;
    }

    public ResolvableType getBoxedType() {
        return boxedType;
    }

    public Supplier<T> getValue() {
        return value;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getAnnotation(Class<A> type) {
        for (Annotation annotation : annotations) {
            if (type.isInstance(annotation)) {
                return (A) annotation;
            }
        }
        return null;
    }

    public boolean hasBindRestriction(BindRestriction bindRestriction) {
        return bindRestrictions.contains(bindRestriction);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Bindable<?> o = (Bindable<?>) obj;
        return nullSafeEquals(type.resolve(), o.type.resolve()) && nullSafeEquals(annotations, o.annotations) && nullSafeEquals(bindRestrictions, o.bindRestrictions);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ObjectUtils.nullSafeHashCode(type);
        result = 31 * result + ObjectUtils.nullSafeHashCode(annotations);
        result = 31 * result + ObjectUtils.nullSafeHashCode(bindRestrictions);
        return result;
    }

    @Override
    public String toString() {
        ToStringCreator creator = new ToStringCreator(this);
        creator.append("type", type);
        creator.append("value", value != null ? "provided" : "none");
        creator.append("annotations", annotations);
        return creator.toString();
    }

    private boolean nullSafeEquals(Object o1, Object o2) {
        return ObjectUtils.nullSafeEquals(o1, o2);
    }

    public Bindable<T> withAnnotations(Annotation... annotations) {
        return new Bindable<>(type, boxedType, value, ObjectUtils.defaultIfNull(annotations, NO_ANNOTATIONS), NO_BIND_RESTRICTIONS);
    }

    public Bindable<T> withExistingValue(T existingValue) {
        Assert.isTrue(existingValue == null || type.isArray() || boxedType.resolve().isInstance(existingValue), () -> "ExistingValue must be an instance of " + type);
        return new Bindable<>(type, boxedType, ObjectUtils.defaultIfNullElseFunction(existingValue, e -> () -> e), annotations, bindRestrictions);
    }

    public Bindable<T> withSuppliedValue(Supplier<T> suppliedValue) {
        return new Bindable<>(type, boxedType, suppliedValue, annotations, bindRestrictions);
    }

    public Bindable<T> withBindRestrictions(BindRestriction... additionalRestrictions) {
        EnumSet<BindRestriction> bindRestrictions = EnumSet.copyOf(this.bindRestrictions);
        bindRestrictions.addAll(ListUtils.newArrayList(additionalRestrictions));
        return new Bindable<>(type, boxedType, value, annotations, bindRestrictions);
    }

    @SuppressWarnings("unchecked")
    public static <T> Bindable<T> ofInstance(T instance) {
        Assert.notNull(instance, "Instance must not be null");
        return of((Class<T>) instance.getClass()).withExistingValue(instance);
    }

    public static <T> Bindable<T> of(Class<T> type) {
        Assert.notNull(type, "Type must not be null");
        return of(ResolvableType.forClass(type));
    }

    public static <E> Bindable<List<E>> listOf(Class<E> elementType) {
        return of(ResolvableType.forClassWithGenerics(List.class, elementType));
    }

    public static <E> Bindable<Set<E>> setOf(Class<E> elementType) {
        return of(ResolvableType.forClassWithGenerics(Set.class, elementType));
    }

    public static <K, V> Bindable<Map<K, V>> mapOf(Class<K> keyType, Class<V> valueType) {
        return of(ResolvableType.forClassWithGenerics(Map.class, keyType, valueType));
    }

    public static <T> Bindable<T> of(ResolvableType type) {
        Assert.notNull(type, "Type must not be null");
        return new Bindable<>(type, box(type), null, NO_ANNOTATIONS, NO_BIND_RESTRICTIONS);
    }

    private static ResolvableType box(ResolvableType type) {
        Class<?> resolved = type.resolve();
        return BooleanUtils.defaultSupplierIfFalse(ObjectUtils.nonNull(resolved) && resolved.isPrimitive(),
                () -> {
                    Object array = Array.newInstance(resolved, 1);
                    Class<?> wrapperType = Array.get(array, 0).getClass();
                    return ResolvableType.forClass(wrapperType);
                },
                () -> BooleanUtils.defaultIfFalse(resolved != null && resolved.isArray(), () -> ResolvableType.forArrayComponent(box(type.getComponentType())), type)
        );
    }

    public enum BindRestriction {
        NO_DIRECT_PROPERTY
    }
}