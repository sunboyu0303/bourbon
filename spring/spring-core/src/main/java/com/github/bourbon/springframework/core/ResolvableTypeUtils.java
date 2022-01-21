package com.github.bourbon.springframework.core;

import org.springframework.core.ResolvableType;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 00:17
 */
public final class ResolvableTypeUtils {

    public static Map<Type, List<Type>> getInterfacesResolvableTypes(Class<?> c) {
        return Arrays.stream(ResolvableType.forClass(c).getInterfaces()).collect(Collectors.toMap(ResolvableType::getType, t -> Arrays.stream(t.getGenerics()).map(ResolvableType::getType).collect(Collectors.toList())));
    }

    public static List<Type> getInterfaceResolvableTypes(Class<?> clazz, Class<?> inter) {
        return Arrays.stream(ResolvableType.forClass(clazz).as(inter).getGenerics()).map(ResolvableType::getType).collect(Collectors.toList());
    }

    public static List<Type> getSuperTypeResolvableTypes(Class<?> c) {
        return Arrays.stream(ResolvableType.forClass(c).getSuperType().getGenerics()).map(ResolvableType::getType).collect(Collectors.toList());
    }

    private ResolvableTypeUtils() {
    }
}