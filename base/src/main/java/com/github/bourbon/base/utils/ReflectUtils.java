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
public interface ReflectUtils {

    SimpleCache<Class<?>, Constructor<?>[]> CONSTRUCTORS_CACHE = new SimpleCache<>();
    SimpleCache<Class<?>, Field[]> FIELDS_CACHE = new SimpleCache<>();
    SimpleCache<Class<?>, Method[]> METHODS_CACHE = new SimpleCache<>();

    @SuppressWarnings("unchecked")
    static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
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

    static <T extends AccessibleObject> T setAccessible(T t) {
        if (null != t && !t.isAccessible()) {
            t.setAccessible(true);
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    static <T> Constructor<T>[] getConstructors(Class<T> c) {
        return (Constructor[]) CONSTRUCTORS_CACHE.computeIfAbsent(c, o -> getConstructorsDirectly(c));
    }

    @SuppressWarnings("unchecked")
    static <T> Constructor<T>[] getConstructorsDirectly(Class<T> c) {
        return (Constructor<T>[]) c.getDeclaredConstructors();
    }

    static String getFieldName(Field f) {
        return ObjectUtils.defaultIfNull(f, Field::getName);
    }

    static Field getField(Class<?> c, String name) {
        return ArrayUtils.firstMatch(f -> name.equals(getFieldName(f)), getFields(c));
    }

    static Field[] getFields(Class<?> c) {
        return FIELDS_CACHE.computeIfAbsent(c, o -> getFieldsDirectly(c, true));
    }

    static Field[] getFieldsDirectly(Class<?> c, boolean withSuper) {
        Field[] allFields = null;
        for (Class<?> tmp = ObjectUtils.requireNonNull(c); tmp != null; tmp = withSuper ? tmp.getSuperclass() : null) {
            allFields = ObjectUtils.isNull(allFields) ? tmp.getDeclaredFields() : ArrayUtils.append(allFields, tmp.getDeclaredFields());
        }
        return allFields;
    }

    static Map<String, Field> getFieldMap(Class<?> beanClass) {
        return Arrays.stream(getFields(beanClass)).collect(Collectors.toMap(Field::getName, f -> f));
    }

    static Object getStaticFieldValue(Field field) throws IllegalAccessException {
        return getFieldValue(null, field);
    }

    static Object getFieldValue(Object obj, Field field) throws IllegalAccessException {
        if (null == field) {
            return null;
        }
        if (obj instanceof Class) {
            obj = null;
        }
        setAccessible(field);
        return field.get(obj);
    }

    static Object[] getFieldsValue(Object obj) throws IllegalAccessException {
        if (ObjectUtils.nonNull(obj)) {
            Field[] fields = getFields(BooleanUtils.defaultSupplierIfAssignableFrom(obj, Class.class, Class.class::cast, obj::getClass));
            if (!ArrayUtils.isEmpty(fields)) {
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

    static Set<String> getPublicMethodNames(Class<?> clazz) {
        return BooleanUtils.defaultSupplierIfPredicate(getPublicMethods(clazz), a -> !ArrayUtils.isEmpty(a), a -> Arrays.stream(a).map(Method::getName).collect(Collectors.toSet()), SetUtils::newHashSet);
    }

    static Method[] getPublicMethods(Class<?> clazz) {
        return ObjectUtils.defaultIfNull(clazz, Class::getMethods);
    }

    static List<Method> getPublicMethods(Class<?> clazz, Predicate<Method> predicate) {
        return ObjectUtils.defaultIfNull(clazz, c -> BooleanUtils.defaultIfPredicate(getPublicMethods(c), m -> !ArrayUtils.isEmpty(m), m -> ObjectUtils.defaultSupplierIfNull(
                predicate, p -> Arrays.stream(m).filter(p).collect(Collectors.toList()), () -> ListUtils.newArrayList(m)
        )));
    }

    static List<Method> getPublicMethods(Class<?> clazz, Method[] excludeMethods) {
        Set<Method> excludeMethodSet = SetUtils.newHashSet(excludeMethods);
        return getPublicMethods(clazz, m -> !excludeMethodSet.contains(m));
    }

    static List<Method> getPublicMethods(Class<?> clazz, String[] excludeMethodNames) {
        Set<String> excludeMethodNameSet = SetUtils.newHashSet(excludeMethodNames);
        return getPublicMethods(clazz, m -> !excludeMethodNameSet.contains(m.getName()));
    }

    static Method getPublicMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    static Method getMethodIgnoreCase(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        return getMethod(clazz, true, methodName, paramTypes);
    }

    static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        return getMethod(clazz, false, methodName, paramTypes);
    }

    static Method getMethod(Class<?> clazz, boolean ignoreCase, String methodName, Class<?>... paramTypes) {
        if (null != clazz && !CharSequenceUtils.isBlank(methodName)) {
            Method[] methods = getMethods(clazz);
            if (!ArrayUtils.isEmpty(methods)) {
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

    static Method getMethodByNameIgnoreCase(Class<?> clazz, String methodName) {
        return getMethodByName(clazz, true, methodName);
    }

    static Method getMethodByName(Class<?> clazz, String methodName) {
        return getMethodByName(clazz, false, methodName);
    }

    static Method getMethodByName(Class<?> clazz, boolean ignoreCase, String methodName) {
        if (null != clazz && !CharSequenceUtils.isBlank(methodName)) {
            Method[] methods = getMethods(clazz);
            if (!ArrayUtils.isEmpty(methods)) {
                for (Method method : methods) {
                    if (CharSequenceUtils.equals(methodName, method.getName(), ignoreCase)) {
                        return method;
                    }
                }
            }
            return null;
        }
        return null;
    }

    static Set<String> getMethodNames(Class<?> c) {
        return Arrays.stream(getMethods(c)).map(Method::getName).collect(Collectors.toSet());
    }

    static Method[] getMethods(Class<?> c) {
        return METHODS_CACHE.computeIfAbsent(c, o -> getMethodsDirectly(c, true));
    }

    static Method[] getMethodsDirectly(Class<?> c, boolean withSuper) {
        Method[] allMethods = null;
        for (Class<?> tmp = c; tmp != null; tmp = withSuper ? tmp.getSuperclass() : null) {
            allMethods = ObjectUtils.isNull(allMethods) ? tmp.getDeclaredMethods() : ArrayUtils.append(allMethods, tmp.getDeclaredMethods());
        }
        return allMethods;
    }

    static Method findMethod(Class<?> clazz, String name) {
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

    static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
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
    static <T> T newInstance(String clazz) {
        try {
            return (T) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(instanceClassErrorMSG(clazz), e);
        }
    }

    static <T> T newInstance(Class<T> clazz, Object[] params) {
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

    static String instanceClassErrorMSG(Object obj) {
        return "Instance class [" + obj + "] error!";
    }
}