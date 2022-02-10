package com.github.bourbon.base.utils;

import com.github.bourbon.base.lang.cache.SimpleCache;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/14 10:47
 */
public final class ReflectUtils {

    private static final SimpleCache<Class<?>, Constructor<?>[]> CONSTRUCTORS_CACHE = new SimpleCache<>();
    private static final SimpleCache<Class<?>, Field[]> FIELDS_CACHE = new SimpleCache<>();
    private static final SimpleCache<Class<?>, Method[]> METHODS_CACHE = new SimpleCache<>();

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        if (null != clazz) {
            for (Constructor<?> c : getConstructors(clazz)) {
                if (ClassUtils.isAllAssignableFrom(c.getParameterTypes(), parameterTypes)) {
                    setAccessible(c);
                    return (Constructor<T>) c;
                }
            }
        }
        return null;
    }

    public static <T extends AccessibleObject> T setAccessible(T t) {
        if (ObjectUtils.nonNull(t) && !t.isAccessible()) {
            t.setAccessible(true);
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T>[] getConstructors(Class<T> c) {
        return (Constructor[]) CONSTRUCTORS_CACHE.computeIfAbsent(c, o -> getConstructorsDirectly(c));
    }

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T>[] getConstructorsDirectly(Class<T> c) {
        return (Constructor<T>[]) c.getDeclaredConstructors();
    }

    public static String getFieldName(Field f) {
        return ObjectUtils.defaultIfNullElseFunction(f, Field::getName);
    }

    public static Field getField(Class<?> c, String name) {
        return ArrayUtils.firstMatch(f -> name.equals(getFieldName(f)), getFields(c));
    }

    public static Field[] getFields(Class<?> c) {
        return FIELDS_CACHE.computeIfAbsent(c, o -> getFieldsDirectly(c, true));
    }

    public static Field[] getFieldsDirectly(Class<?> c, boolean withSuper) {
        Field[] allFields = null;
        for (Class<?> tmp = ObjectUtils.requireNonNull(c); tmp != null; tmp = withSuper ? tmp.getSuperclass() : null) {
            allFields = ObjectUtils.isNull(allFields) ? tmp.getDeclaredFields() : ArrayUtils.append(allFields, tmp.getDeclaredFields());
        }
        return allFields;
    }

    public static Map<String, Field> getFieldMap(Class<?> beanClass) {
        return Arrays.stream(getFields(beanClass)).collect(Collectors.toMap(Field::getName, f -> f));
    }

    public static Object getStaticFieldValue(Field field) throws IllegalAccessException {
        return getFieldValue(null, field);
    }

    public static Object getFieldValue(Object obj, Field field) throws IllegalAccessException {
        if (null == field) {
            return null;
        }
        if (obj instanceof Class) {
            obj = null;
        }
        setAccessible(field);
        return field.get(obj);
    }

    public static Object[] getFieldsValue(Object obj) throws IllegalAccessException {
        if (ObjectUtils.nonNull(obj)) {
            Field[] fields = getFields(BooleanUtils.defaultSupplierIfAssignableFrom(obj, Class.class, Class.class::cast, obj::getClass));
            if (ArrayUtils.isNotEmpty(fields)) {
                int len = fields.length;
                Object[] values = new Object[len];
                for (int i = 0; i < len; ++i) {
                    values[i] = getFieldValue(obj, fields[i]);
                }
                return values;
            }
        }
        return null;
    }

    public static Set<String> getPublicMethodNames(Class<?> clazz) {
        return BooleanUtils.defaultSupplierIfPredicate(getPublicMethods(clazz), ArrayUtils::isNotEmpty, a -> Arrays.stream(a).map(Method::getName).collect(Collectors.toSet()), SetUtils::newHashSet);
    }

    public static Method[] getPublicMethods(Class<?> clazz) {
        return ObjectUtils.defaultIfNullElseFunction(clazz, Class::getMethods);
    }

    public static List<Method> getPublicMethods(Class<?> clazz, Predicate<Method> predicate) {
        return ObjectUtils.defaultIfNullElseFunction(clazz, c -> BooleanUtils.defaultIfPredicate(getPublicMethods(c), ArrayUtils::isNotEmpty, m -> ObjectUtils.defaultSupplierIfNullElseFunction(
                predicate, p -> Arrays.stream(m).filter(p).collect(Collectors.toList()), () -> ListUtils.newArrayList(m)
        )));
    }

    public static List<Method> getPublicMethods(Class<?> clazz, Method[] excludeMethods) {
        Set<Method> excludeMethodSet = SetUtils.newHashSet(excludeMethods);
        return getPublicMethods(clazz, m -> !excludeMethodSet.contains(m));
    }

    public static List<Method> getPublicMethods(Class<?> clazz, String[] excludeMethodNames) {
        Set<String> excludeMethodNameSet = SetUtils.newHashSet(excludeMethodNames);
        return getPublicMethods(clazz, m -> !excludeMethodNameSet.contains(m.getName()));
    }

    public static Method getPublicMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getMethodIgnoreCase(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        return getMethod(clazz, true, methodName, paramTypes);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        return getMethod(clazz, false, methodName, paramTypes);
    }

    public static Method getMethod(Class<?> clazz, boolean ignoreCase, String methodName, Class<?>... paramTypes) {
        if (null != clazz && !CharSequenceUtils.isBlank(methodName)) {
            Method[] methods = getMethods(clazz);
            if (ArrayUtils.isNotEmpty(methods)) {
                for (Method method : methods) {
                    if (CharSequenceUtils.equals(methodName, method.getName(), ignoreCase) && ClassUtils.isAllAssignableFrom(method.getParameterTypes(), paramTypes)) {
                        return method;
                    }
                }
            }
            return null;
        }
        return null;
    }

    public static Method getMethodByNameIgnoreCase(Class<?> clazz, String methodName) {
        return getMethodByName(clazz, true, methodName);
    }

    public static Method getMethodByName(Class<?> clazz, String methodName) {
        return getMethodByName(clazz, false, methodName);
    }

    public static Method getMethodByName(Class<?> clazz, boolean ignoreCase, String methodName) {
        if (null != clazz && !CharSequenceUtils.isBlank(methodName)) {
            Method[] methods = getMethods(clazz);
            if (ArrayUtils.isNotEmpty(methods)) {
                for (Method method : methods) {
                    if (CharSequenceUtils.equals(methodName, method.getName(), ignoreCase)) {
                        return method;
                    }
                }
            }
        }
        return null;
    }

    public static Set<String> getMethodNames(Class<?> c) {
        return Arrays.stream(getMethods(c)).map(Method::getName).collect(Collectors.toSet());
    }

    public static Method[] getMethods(Class<?> c) {
        return METHODS_CACHE.computeIfAbsent(c, o -> getMethodsDirectly(c, true));
    }

    public static Method[] getMethodsDirectly(Class<?> c, boolean withSuper) {
        Method[] allMethods = null;
        for (Class<?> tmp = c; tmp != null; tmp = withSuper ? tmp.getSuperclass() : null) {
            allMethods = ObjectUtils.isNull(allMethods) ? tmp.getDeclaredMethods() : ArrayUtils.append(allMethods, tmp.getDeclaredMethods());
        }
        return allMethods;
    }

    public static Method findMethod(Class<?> clazz, String name) {
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (Method method : methods) {
                if (name.equals(method.getName())) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Class<?> searchType = clazz;
        while (searchType != null) {
            try {
                return paramTypes == null ? findMethod(clazz, name) : clazz.getMethod(name, paramTypes);
            } catch (NoSuchMethodException e) {
                searchType = searchType.getSuperclass();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String clazz) {
        try {
            return (T) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(instanceClassErrorMSG(clazz), e);
        }
    }

    public static <T> T newInstance(Class<T> clazz, Object[] params) {
        if (ArrayUtils.isEmpty(params)) {
            try {
                return getConstructor(clazz).newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException(instanceClassErrorMSG(clazz), e);
            }
        } else {
            Class<?>[] paramTypes = ClassUtils.getClasses(params);
            Constructor<T> constructor = getConstructor(clazz, paramTypes);
            if (null == constructor) {
                throw new NullPointerException("No Constructor matched for parameter types: [" + ListUtils.newArrayList(paramTypes) + "]");
            }
            try {
                return constructor.newInstance(params);
            } catch (Exception e) {
                throw new IllegalArgumentException(instanceClassErrorMSG(clazz), e);
            }
        }
    }

    private static String instanceClassErrorMSG(Object obj) {
        return "Instance class [" + obj + "] error!";
    }

    public static Set<Class<?>> getAllInterface(Class<?> type) {
        return getAllInterfaceAndClass(type).stream().filter(Class::isInterface).collect(Collectors.toSet());
    }

    public static Set<Class<?>> getAllInterfaceAndClass(Class<?> type) {
        Set<Class<?>> result = SetUtils.newHashSet(type);
        findAllInterfaceAndSuperClass(type, result);
        return result;
    }

    private static void findAllInterfaceAndSuperClass(Class<?> type, Set<Class<?>> result) {
        Class[] interfaces = type.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class<?> anInterface : interfaces) {
                if (result.add(anInterface)) {
                    findAllInterfaceAndSuperClass(anInterface, result);
                }
            }
        }

        if (!type.isInterface()) {
            Class<?> superclass = type.getSuperclass();
            if (superclass != null && !Object.class.equals(superclass)) {
                result.add(superclass);
                findAllInterfaceAndSuperClass(superclass, result);
            }
        }
    }
}