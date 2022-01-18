package com.github.bourbon.base.lang;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/11 16:28
 */
public interface Assert {

    static void isTrue(boolean expression, String message) throws IllegalArgumentException {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isTrue(boolean expression, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (!expression) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void isTrue(boolean expression, String format, Object... args) throws IllegalArgumentException {
        if (!expression) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void isTrue(boolean expression, ThrowableSupplier<X> supplier) throws X {
        if (!expression) {
            throw supplier.get();
        }
    }

    static void notTrue(boolean expression, String message) throws IllegalArgumentException {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notTrue(boolean expression, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (expression) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notTrue(boolean expression, String format, Object... args) throws IllegalArgumentException {
        if (expression) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notTrue(boolean expression, ThrowableSupplier<X> supplier) throws X {
        if (expression) {
            throw supplier.get();
        }
    }

    static void isNull(Object object, String message) throws IllegalArgumentException {
        if (ObjectUtils.nonNull(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isNull(Object object, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (ObjectUtils.nonNull(object)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void isNull(Object object, String format, Object... args) throws IllegalArgumentException {
        if (ObjectUtils.nonNull(object)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void isNull(Object object, ThrowableSupplier<X> supplier) throws X {
        if (ObjectUtils.nonNull(object)) {
            throw supplier.get();
        }
    }

    static void isNull(String s, String message) throws IllegalArgumentException {
        if (!CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isNull(String s, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (!CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void isNull(String s, String format, Object... args) throws IllegalArgumentException {
        if (!CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void isNull(String s, ThrowableSupplier<X> supplier) throws X {
        if (!CharSequenceUtils.isEmpty(s)) {
            throw supplier.get();
        }
    }

    static void isNull(CharSequence s, String message) throws IllegalArgumentException {
        if (!CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isNull(CharSequence s, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (!CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void isNull(CharSequence s, String format, Object... args) throws IllegalArgumentException {
        if (!CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void isNull(CharSequence s, ThrowableSupplier<X> supplier) throws X {
        if (!CharSequenceUtils.isEmpty(s)) {
            throw supplier.get();
        }
    }

    static void notNull(Object object, String message) throws IllegalArgumentException {
        if (ObjectUtils.isNull(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notNull(Object object, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (ObjectUtils.isNull(object)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notNull(Object object, String format, Object... args) throws IllegalArgumentException {
        if (ObjectUtils.isNull(object)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notNull(Object object, ThrowableSupplier<X> supplier) throws X {
        if (ObjectUtils.isNull(object)) {
            throw supplier.get();
        }
    }

    static void notNull(String s, String message) throws IllegalArgumentException {
        if (CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notNull(String s, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notNull(String s, String format, Object... args) throws IllegalArgumentException {
        if (CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notNull(String s, ThrowableSupplier<X> supplier) throws X {
        if (CharSequenceUtils.isEmpty(s)) {
            throw supplier.get();
        }
    }

    static void notNull(CharSequence s, String message) throws IllegalArgumentException {
        if (CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notNull(CharSequence s, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notNull(CharSequence s, String format, Object... args) throws IllegalArgumentException {
        if (CharSequenceUtils.isEmpty(s)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notNull(CharSequence s, ThrowableSupplier<X> supplier) throws X {
        if (CharSequenceUtils.isEmpty(s)) {
            throw supplier.get();
        }
    }

    static void notBlank(String s, String message) throws IllegalArgumentException {
        if (CharSequenceUtils.isBlank(s)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notBlank(String s, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (CharSequenceUtils.isBlank(s)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notBlank(String s, String format, Object... args) throws IllegalArgumentException {
        if (CharSequenceUtils.isBlank(s)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notBlank(String s, ThrowableSupplier<X> supplier) throws X {
        if (CharSequenceUtils.isBlank(s)) {
            throw supplier.get();
        }
    }

    static void notBlank(CharSequence s, String message) throws IllegalArgumentException {
        if (CharSequenceUtils.isBlank(s)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notBlank(CharSequence s, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (CharSequenceUtils.isBlank(s)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notBlank(CharSequence s, String format, Object... args) throws IllegalArgumentException {
        if (CharSequenceUtils.isBlank(s)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notBlank(CharSequence s, ThrowableSupplier<X> supplier) throws X {
        if (CharSequenceUtils.isBlank(s)) {
            throw supplier.get();
        }
    }

    static void notEmpty(Object[] array, String message) throws IllegalArgumentException {
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notEmpty(Object[] array, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notEmpty(Object[] array, String format, Object... args) throws IllegalArgumentException {
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notEmpty(Object[] array, ThrowableSupplier<X> supplier) throws X {
        if (ArrayUtils.isEmpty(array)) {
            throw supplier.get();
        }
    }

    static void notEmpty(boolean[] array, String message) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notEmpty(boolean[] array, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notEmpty(boolean[] array, String format, Object... args) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notEmpty(boolean[] array, ThrowableSupplier<X> supplier) throws X {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw supplier.get();
        }
    }

    static void notEmpty(float[] array, String message) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notEmpty(float[] array, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notEmpty(float[] array, String format, Object... args) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notEmpty(float[] array, ThrowableSupplier<X> supplier) throws X {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw supplier.get();
        }
    }

    static void notEmpty(double[] array, String message) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notEmpty(double[] array, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notEmpty(double[] array, String format, Object... args) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notEmpty(double[] array, ThrowableSupplier<X> supplier) throws X {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw supplier.get();
        }
    }

    static void notEmpty(byte[] array, String message) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notEmpty(byte[] array, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notEmpty(byte[] array, String format, Object... args) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notEmpty(byte[] array, ThrowableSupplier<X> supplier) throws X {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw supplier.get();
        }
    }

    static void notEmpty(short[] array, String message) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notEmpty(short[] array, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notEmpty(short[] array, String format, Object... args) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notEmpty(short[] array, ThrowableSupplier<X> supplier) throws X {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw supplier.get();
        }
    }

    static void notEmpty(int[] array, String message) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notEmpty(int[] array, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notEmpty(int[] array, String format, Object... args) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notEmpty(int[] array, ThrowableSupplier<X> supplier) throws X {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw supplier.get();
        }
    }

    static void notEmpty(long[] array, String message) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notEmpty(long[] array, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notEmpty(long[] array, String format, Object... args) throws IllegalArgumentException {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notEmpty(long[] array, ThrowableSupplier<X> supplier) throws X {
        if (PrimitiveArrayUtils.isEmpty(array)) {
            throw supplier.get();
        }
    }

    static void isEmpty(Object[] array, String message) throws IllegalArgumentException {
        if (!ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isEmpty(Object[] array, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (!ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void isEmpty(Object[] array, String format, Object... args) throws IllegalArgumentException {
        if (!ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void isEmpty(Object[] array, ThrowableSupplier<X> supplier) throws X {
        if (!ArrayUtils.isEmpty(array)) {
            throw supplier.get();
        }
    }

    static void notEmpty(Collection<?> collection, String message) throws IllegalArgumentException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notEmpty(Collection<?> collection, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notEmpty(Collection<?> collection, String format, Object... args) throws IllegalArgumentException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notEmpty(Collection<?> collection, ThrowableSupplier<X> supplier) throws X {
        if (CollectionUtils.isEmpty(collection)) {
            throw supplier.get();
        }
    }

    static void isEmpty(Collection<?> collection, String message) throws IllegalArgumentException {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isEmpty(Collection<?> collection, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void isEmpty(Collection<?> collection, String format, Object... args) throws IllegalArgumentException {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void isEmpty(Collection<?> collection, ThrowableSupplier<X> supplier) throws X {
        if (!CollectionUtils.isEmpty(collection)) {
            throw supplier.get();
        }
    }

    static void notEmpty(Map<?, ?> map, String message) throws IllegalArgumentException {
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void notEmpty(Map<?, ?> map, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void notEmpty(Map<?, ?> map, String format, Object... args) throws IllegalArgumentException {
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void notEmpty(Map<?, ?> map, ThrowableSupplier<X> supplier) throws X {
        if (MapUtils.isEmpty(map)) {
            throw supplier.get();
        }
    }

    static void isEmpty(Map<?, ?> map, String message) throws IllegalArgumentException {
        if (!MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void isEmpty(Map<?, ?> map, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (!MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    static void isEmpty(Map<?, ?> map, String format, Object... args) throws IllegalArgumentException {
        if (!MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(String.format(format, args));
        }
    }

    static <X extends Throwable> void isEmpty(Map<?, ?> map, ThrowableSupplier<X> supplier) throws X {
        if (!MapUtils.isEmpty(map)) {
            throw supplier.get();
        }
    }

    static void isInstanceOf(Class<?> type, Object obj) throws IllegalArgumentException {
        isInstanceOf(type, obj, StringConstants.EMPTY);
    }

    static void isInstanceOf(Class<?> type, Object obj, String message) throws IllegalArgumentException {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, message);
        }
    }

    static void isInstanceOf(Class<?> type, Object obj, Supplier<String> messageSupplier) throws IllegalArgumentException {
        isInstanceOf(type, obj, messageSupplier.get());
    }

    static void instanceCheckFailed(Class<?> type, Object obj, String message) throws IllegalArgumentException {
        if (CharSequenceUtils.isEmpty(message)) {
            String className = ObjectUtils.defaultIfNull(obj, o -> o.getClass().getName(), StringConstants.NULL);
            throw new IllegalArgumentException("Object of class [" + className + "] must be an instance of " + type);
        }
        throw new IllegalArgumentException(message);
    }

    static void isAssignable(Class<?> superType, Class<?> subType) throws IllegalArgumentException {
        isAssignable(superType, subType, StringConstants.EMPTY);
    }

    static void isAssignable(Class<?> superType, Class<?> subType, String message) throws IllegalArgumentException {
        notNull(superType, "Super type to check against must not be null");
        if (ObjectUtils.isNull(subType) || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, message);
        }
    }

    static void isAssignable(Class<?> superType, Class<?> subType, Supplier<String> messageSupplier) throws IllegalArgumentException {
        isAssignable(superType, subType, messageSupplier.get());
    }

    static <X extends Throwable> void isAssignable(Class<?> superType, Class<?> subType, ThrowableSupplier<X> supplier) throws X {
        notNull(superType, supplier);
        if (ObjectUtils.isNull(subType) || !superType.isAssignableFrom(subType)) {
            throw supplier.get();
        }
    }

    static void assignableCheckFailed(Class<?> superType, Class<?> subType, String message) throws IllegalArgumentException {
        if (CharSequenceUtils.isEmpty(message)) {
            throw new IllegalArgumentException(subType + " is not assignable to " + superType);
        }
        throw new IllegalArgumentException(message);
    }
}