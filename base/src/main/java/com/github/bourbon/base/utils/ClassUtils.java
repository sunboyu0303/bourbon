package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.*;
import com.github.bourbon.base.convert.BasicType;
import com.github.bourbon.base.lang.Assert;

import java.beans.Introspector;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 17:05
 */
public interface ClassUtils {

    @SuppressWarnings("unchecked")
    static <T> Class<T> getClass(T t) {
        return ObjectUtils.defaultIfNull(t, c -> (Class<T>) c.getClass());
    }

    static String getClassName(Object o) {
        return getClassName(o, false);
    }

    static String getClassName(Object obj, boolean isSimple) {
        return ObjectUtils.defaultIfNull(obj, o -> getClassName(o.getClass(), isSimple));
    }

    static String getClassName(Class<?> c) {
        return getClassName(c, false);
    }

    static String getClassName(Class<?> clazz, boolean isSimple) {
        return ObjectUtils.defaultIfNull(clazz, c -> BooleanUtils.defaultSupplierIfFalse(isSimple, c::getSimpleName, c::getName));
    }

    static String getSimpleClassName(Class<?> c) {
        return getClassName(c, true);
    }

    static String getSimpleClassName(Object o) {
        return getClassName(o, true);
    }

    static String getShortClassName(Class<?> c) {
        return getShortClassName(c.getName());
    }

    static String getShortClassName(String className) {
        List<String> packages = CharSequenceUtils.split(className, CharConstants.DOT);
        if (ObjectUtils.nonNull(packages) && packages.size() >= 2) {
            int size = packages.size();
            StringBuilder result = new StringBuilder();
            result.append((packages.get(0)).charAt(0));
            for (int i = 1; i < size - 1; ++i) {
                result.append(CharConstants.DOT).append((packages.get(i)).charAt(0));
            }
            result.append(CharConstants.DOT).append(packages.get(size - 1));
            return result.toString();
        }
        return className;
    }

    static boolean isAssignableFrom(Class<?> superType, Class<?> targetType) {
        if (superType == null || targetType == null) {
            return false;
        }
        if (ObjectUtils.equals(superType, targetType)) {
            return true;
        }
        return superType.isAssignableFrom(targetType);
    }

    static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2) {
        if (ArrayUtils.isEmpty(types1) && ArrayUtils.isEmpty(types2)) {
            return true;
        }
        if (null != types1 && null != types2) {
            if (types1.length != types2.length) {
                return false;
            }
            for (int i = 0; i < types1.length; ++i) {
                Class<?> type1 = types1[i];
                Class<?> type2 = types2[i];
                if (isPrimitive(type1) && isPrimitive(type2)) {
                    if (BasicType.unWrap(type1) != BasicType.unWrap(type2)) {
                        return false;
                    }
                } else if (!type1.isAssignableFrom(type2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    static boolean isPrimitive(Class<?> c) {
        return null != c && (c.isPrimitive() || isPrimitiveWrapper(c));
    }

    static boolean isPrimitiveArray(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return clazz.isArray() && clazz.getComponentType().isPrimitive();
    }

    static boolean isPrimitiveWrapper(Class<?> clazz) {
        return null != clazz && BasicType.containsWrapper(clazz);
    }

    static boolean isPrimitiveWrapperArray(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType());
    }

    static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return clazz.isPrimitive() || isPrimitiveWrapper(clazz);
    }

    static Class<?>[] getClasses(Object[] objects) {
        Class<?>[] classes = new Class[objects.length];
        for (int i = 0; i < objects.length; ++i) {
            classes[i] = ObjectUtils.defaultIfNull(objects[i], Object::getClass, Object.class);
        }
        return classes;
    }

    static Object[] getDefaultValues(Class<?>[] classes) {
        Object[] values = new Object[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            values[i] = getDefaultValue(classes[i]);
        }
        return values;
    }

    static Object getDefaultValue(Class<?> c) {
        if (!c.isPrimitive()) {
            return null;
        }
        if (LongConstants.TYPE == c) {
            return LongConstants.DEFAULT;
        }
        if (IntConstants.TYPE == c) {
            return IntConstants.DEFAULT;
        }
        if (ShortConstants.TYPE == c) {
            return ShortConstants.DEFAULT;
        }
        if (ByteConstants.TYPE == c) {
            return ByteConstants.DEFAULT;
        }
        if (DoubleConstants.TYPE == c) {
            return DoubleConstants.DEFAULT;
        }
        if (FloatConstants.TYPE == c) {
            return FloatConstants.DEFAULT;
        }
        if (CharConstants.TYPE == c) {
            return CharConstants.DEFAULT;
        }
        if (BooleanConstants.TYPE == c) {
            return BooleanConstants.DEFAULT;
        }
        return null;
    }

    static boolean isAbstract(Class<?> c) {
        return Modifier.isAbstract(c.getModifiers());
    }

    static boolean isNormalClass(Class<?> c) {
        return null != c && !c.isInterface() && !isAbstract(c) && !c.isEnum() && !c.isArray() && !c.isAnnotation() && !c.isSynthetic() && !c.isPrimitive();
    }

    static <T> Class<T> getTypeArgument(Class<T> c) {
        return getTypeArgument(c, 0);
    }

    static <T> Class<T> getTypeArgument(Class<T> c, int i) {
        return TypeUtils.getClass(TypeUtils.getTypeArgument(c, i));
    }

    static String lowerFirst(Class<?> clazz) {
        return ObjectUtils.defaultIfNull(clazz, c -> CharSequenceUtils.lowerFirst(c.getSimpleName()));
    }

    static String upperFirst(Class<?> clazz) {
        return ObjectUtils.defaultIfNull(clazz, c -> CharSequenceUtils.upperFirst(c.getSimpleName()));
    }

    static boolean isCglibProxy(Object object) {
        return isCglibProxyClass(object.getClass());
    }

    static boolean isCglibProxyClass(Class<?> c) {
        return !ObjectUtils.isNull(c) && isCglibProxyClassName(c.getName());
    }

    static boolean isCglibProxyClassName(String name) {
        return !CharSequenceUtils.isEmpty(name) && name.contains(StringConstants.DOLLARS);
    }

    static String getPackageName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return getPackageName(clazz.getName());
    }

    static String getPackageName(String className) {
        Assert.notNull(className, "Class name must not be null");
        return BooleanUtils.defaultIfPredicate(className.lastIndexOf(CharConstants.DOT), idx -> idx != -1, idx -> className.substring(0, idx), StringConstants.EMPTY);
    }

    static String getClassFileName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return getClassFileName(clazz.getName());
    }

    static String getClassFileName(String fqClassName) {
        Assert.notNull(fqClassName, "Class name must not be null");
        return fqClassName.substring(fqClassName.lastIndexOf(CharConstants.DOT) + 1) + StringConstants.CLASS_FILE_SUFFIX;
    }

    static String getShortNameAsProperty(Class<?> clazz) {
        String shortName = getShortClassName(clazz);
        return Introspector.decapitalize(BooleanUtils.defaultIfPredicate(shortName.lastIndexOf(CharConstants.DOT), dotIndex -> dotIndex != -1, dotIndex -> shortName.substring(dotIndex + 1), shortName));
    }

    static boolean isPresent(String className) {
        return isPresent(className, null);
    }

    static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            forName(className, classLoader);
            return true;
        } catch (IllegalAccessError err) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" + className + "]: " + err.getMessage(), err);
        } catch (Throwable ex) {
            return false;
        }
    }

    static Class<?> forName(String name) {
        return forName(name, ClassLoaderUtils.getClassLoader());
    }

    static Class<?> forName(String name, ClassLoader classLoader) {

        Assert.notNull(name, "Name must not be null");

        Class<?> clazz = resolvePrimitiveClassName(name);
        if (clazz == null) {
            clazz = BasicType.getWrapper(name);
        }
        if (clazz != null) {
            return clazz;
        }

        // "java.lang.String[]" style arrays
        if (name.endsWith(StringConstants.LEFT_RIGHT_BRACKETS)) {
            String elementClassName = name.substring(0, name.length() - StringConstants.LEFT_RIGHT_BRACKETS.length());
            return Array.newInstance(forName(elementClassName, classLoader), 0).getClass();
        }

        // "[Ljava.lang.String;" style arrays
        if (name.startsWith(StringConstants.LEFT_BRACKETS_L) && name.endsWith(StringConstants.SEMICOLON)) {
            String elementName = name.substring(StringConstants.LEFT_BRACKETS_L.length(), name.length() - 1);
            return Array.newInstance(forName(elementName, classLoader), 0).getClass();
        }

        // "[[I" or "[[Ljava.lang.String;" style arrays
        if (name.startsWith(StringConstants.LEFT_BRACKETS)) {
            String elementName = name.substring(StringConstants.LEFT_BRACKETS.length());
            return Array.newInstance(forName(elementName, classLoader), 0).getClass();
        }

        ClassLoader clToUse = classLoader;
        if (clToUse == null) {
            clToUse = ClassLoaderUtils.getClassLoader();
        }
        try {
            return Class.forName(name, false, clToUse);
        } catch (ClassNotFoundException ex) {
            int lastDotIndex = name.lastIndexOf(CharConstants.DOT);
            if (lastDotIndex != -1) {
                String nestedClassName = name.substring(0, lastDotIndex) + StringConstants.DOLLAR + name.substring(lastDotIndex + 1);
                try {
                    return Class.forName(nestedClassName, false, clToUse);
                } catch (ClassNotFoundException ex2) {
                    // Swallow - let original exception get through
                }
            }
            throw new IllegalArgumentException(ex);
        }
    }

    static Class<?> resolvePrimitiveClassName(String name) {
        Class<?> result = null;
        if (name != null && name.length() <= 7) {
            result = BasicType.getPrimitive(name);
        }
        return result;
    }

    static Class<?> resolveClassName(String className) throws IllegalArgumentException {
        return resolveClassName(className, ClassLoaderUtils.getClassLoader());
    }

    static Class<?> resolveClassName(String className, ClassLoader classLoader) throws IllegalArgumentException {
        try {
            return forName(className, classLoader);
        } catch (IllegalAccessError err) {
            throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" + className + "]: " + err.getMessage(), err);
        } catch (LinkageError err) {
            throw new IllegalArgumentException("Unresolvable class definition for class [" + className + "]", err);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not find class [" + className + "]", ex);
        }
    }

    static Class<?> resolvePrimitiveIfNecessary(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return BooleanUtils.defaultIfFalse(clazz.isPrimitive() && clazz != void.class, () -> BasicType.getWrapper(clazz), clazz);
    }

    static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {
        if (classLoader == null) {
            return true;
        }
        try {
            if (clazz.getClassLoader() == classLoader) {
                return true;
            }
        } catch (SecurityException ignore) {
        }
        return isLoadable(clazz, classLoader);
    }

    static boolean isLoadable(Class<?> clazz, ClassLoader classLoader) {
        try {
            return clazz == classLoader.loadClass(clazz.getName());
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    static String classNamesToString(Class<?>... classes) {
        return classNamesToString(ListUtils.newArrayList(classes));
    }

    static String classNamesToString(Collection<Class<?>> classes) {
        if (CollectionUtils.isEmpty(classes)) {
            return StringConstants.LEFT_RIGHT_BRACKETS;
        }
        StringJoiner stringJoiner = new StringJoiner(StringConstants.COMMA_SPACE, StringConstants.LEFT_BRACKETS, StringConstants.RIGHT_BRACKETS);
        for (Class<?> clazz : classes) {
            stringJoiner.add(clazz.getName());
        }
        return stringJoiner.toString();
    }
}