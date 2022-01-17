package com.github.bourbon.base.convert;

import com.github.bourbon.base.constant.*;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;

import java.io.Closeable;
import java.io.Externalizable;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 22:51
 */
public enum BasicType {
    BOOLEAN(BooleanConstants.PRIMITIVE_CLASS, BooleanConstants.BOXED_CLASS),
    CHARACTER(CharConstants.PRIMITIVE_CLASS, CharConstants.BOXED_CLASS),
    FLOAT(FloatConstants.PRIMITIVE_CLASS, FloatConstants.BOXED_CLASS),
    DOUBLE(DoubleConstants.PRIMITIVE_CLASS, DoubleConstants.BOXED_CLASS),
    BYTE(ByteConstants.PRIMITIVE_CLASS, ByteConstants.BOXED_CLASS),
    SHORT(ShortConstants.PRIMITIVE_CLASS, ShortConstants.BOXED_CLASS),
    INTEGER(IntConstants.PRIMITIVE_CLASS, IntConstants.BOXED_CLASS),
    LONG(LongConstants.PRIMITIVE_CLASS, LongConstants.BOXED_CLASS),
    VOID(VoidConstants.PRIMITIVE_CLASS, VoidConstants.BOXED_CLASS);

    private static final Map<Class<?>, Class<?>> wrapperTypeToPrimitiveTypeMap = new IdentityHashMap<>(9);
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperTypeMap = new IdentityHashMap<>(9);
    private static final Map<String, Class<?>> primitiveTypeNameToPrimitiveTypeMap = MapUtils.newHashMap(32);
    private static final Map<String, Class<?>> wrapperTypeNameToWrapperTypeMap = MapUtils.newHashMap(64);
    private static final Set<Class<?>> javaLanguageInterfaces;

    static {
        Arrays.stream(values()).forEach(basicType -> {
            wrapperTypeToPrimitiveTypeMap.put(basicType.boxedClass, basicType.primitiveClass);
            primitiveTypeToWrapperTypeMap.put(basicType.primitiveClass, basicType.boxedClass);

            registerClasses(basicType.boxedClass);
        });

        Set<Class<?>> primitiveTypes = SetUtils.newHashSet(32);
        primitiveTypes.addAll(wrapperTypeToPrimitiveTypeMap.values());
        Collections.addAll(primitiveTypes, boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class, long[].class, short[].class);
        for (Class<?> primitiveType : primitiveTypes) {
            primitiveTypeNameToPrimitiveTypeMap.put(primitiveType.getName(), primitiveType);
        }

        registerClasses(Boolean[].class, Character[].class, Float[].class, Double[].class, Byte[].class, Short[].class, Integer[].class, Long[].class);

        registerClasses(Number.class, Number[].class, String.class, String[].class, Class.class, Class[].class, Object.class, Object[].class);

        registerClasses(Throwable.class, Exception.class, RuntimeException.class, Error.class, StackTraceElement.class, StackTraceElement[].class);

        registerClasses(Enum.class, Iterable.class, Iterator.class, Enumeration.class, Collection.class, List.class, Set.class, Map.class, Map.Entry.class, Optional.class);

        Class<?>[] javaLanguageInterfaceArray = {Serializable.class, Externalizable.class, Closeable.class, AutoCloseable.class, Cloneable.class, Comparable.class};

        registerClasses(javaLanguageInterfaceArray);

        javaLanguageInterfaces = SetUtils.newHashSet(javaLanguageInterfaceArray);
    }

    private static void registerClasses(Class<?>... commonClasses) {
        wrapperTypeNameToWrapperTypeMap.putAll(Arrays.stream(commonClasses).collect(Collectors.toMap(Class::getName, c -> c)));
    }

    public static boolean containsWrapper(Class<?> clazz) {
        return wrapperTypeToPrimitiveTypeMap.containsKey(clazz);
    }

    public static Class<?> getPrimitive(Class<?> clazz) {
        return primitiveTypeToWrapperTypeMap.get(clazz);
    }

    public static Class<?> getPrimitive(String className) {
        return primitiveTypeNameToPrimitiveTypeMap.get(className);
    }

    public static boolean containsPrimitive(Class<?> clazz) {
        return primitiveTypeToWrapperTypeMap.containsKey(clazz);
    }

    public static Class<?> getWrapper(Class<?> clazz) {
        return primitiveTypeToWrapperTypeMap.get(clazz);
    }

    public static Class<?> getWrapper(String className) {
        return wrapperTypeNameToWrapperTypeMap.get(className);
    }

    public static Class<?> wrap(Class<?> clazz) {
        return BooleanUtils.defaultIfPredicate(clazz, c -> ObjectUtils.nonNull(c) && c.isPrimitive(), c -> ObjectUtils.defaultIfNull(primitiveTypeToWrapperTypeMap.get(c), c), clazz);
    }

    public static Class<?> unWrap(Class<?> clazz) {
        return BooleanUtils.defaultIfPredicate(clazz, c -> ObjectUtils.nonNull(c) && !c.isPrimitive(), c -> ObjectUtils.defaultIfNull(wrapperTypeToPrimitiveTypeMap.get(c), c), clazz);
    }

    private final Class<?> primitiveClass;
    private final Class<?> boxedClass;

    BasicType(Class<?> primitiveClass, Class<?> boxedClass) {
        this.primitiveClass = primitiveClass;
        this.boxedClass = boxedClass;
    }
}