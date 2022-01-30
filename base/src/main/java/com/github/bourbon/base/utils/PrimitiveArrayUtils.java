package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.*;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/13 09:20
 */
public interface PrimitiveArrayUtils {

    static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    static boolean isNotEmpty(boolean[] array) {
        return !isEmpty(array);
    }

    static boolean isNotEmpty(char[] array) {
        return !isEmpty(array);
    }

    static boolean isNotEmpty(float[] array) {
        return !isEmpty(array);
    }

    static boolean isNotEmpty(double[] array) {
        return !isEmpty(array);
    }

    static boolean isNotEmpty(byte[] array) {
        return !isEmpty(array);
    }

    static boolean isNotEmpty(short[] array) {
        return !isEmpty(array);
    }

    static boolean isNotEmpty(int[] array) {
        return !isEmpty(array);
    }

    static boolean isNotEmpty(long[] array) {
        return !isEmpty(array);
    }

    static boolean contains(boolean[] array, boolean objectToFind) {
        return indexOf(array, objectToFind, 0) != -1;
    }

    static boolean contains(char[] array, char objectToFind) {
        return indexOf(array, objectToFind, 0) != -1;
    }

    static boolean contains(float[] array, float objectToFind) {
        return indexOf(array, objectToFind, 0) != -1;
    }

    static boolean contains(double[] array, double objectToFind) {
        return indexOf(array, objectToFind, 0) != -1;
    }

    static boolean contains(byte[] array, byte objectToFind) {
        return indexOf(array, objectToFind, 0) != -1;
    }

    static boolean contains(short[] array, short objectToFind) {
        return indexOf(array, objectToFind, 0) != -1;
    }

    static boolean contains(int[] array, int objectToFind) {
        return indexOf(array, objectToFind, 0) != -1;
    }

    static boolean contains(long[] array, long objectToFind) {
        return indexOf(array, objectToFind, 0) != -1;
    }

    static int indexOf(boolean[] array, boolean value) {
        return indexOf(array, value, 0);
    }

    static int indexOf(char[] array, char value) {
        return indexOf(array, value, 0);
    }

    static int indexOf(float[] array, float value) {
        return indexOf(array, value, 0);
    }

    static int indexOf(double[] array, double value) {
        return indexOf(array, value, 0);
    }

    static int indexOf(byte[] array, byte value) {
        return indexOf(array, value, 0);
    }

    static int indexOf(short[] array, short value) {
        return indexOf(array, value, 0);
    }

    static int indexOf(int[] array, int value) {
        return indexOf(array, value, 0);
    }

    static int indexOf(long[] array, long value) {
        return indexOf(array, value, 0);
    }

    static int indexOf(boolean[] array, boolean value, int beginIndex) {
        if (!isEmpty(array)) {
            int len = array.length;
            for (int i = beginIndex; i < len; ++i) {
                if (BooleanUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int indexOf(char[] array, char value, int beginIndex) {
        if (!isEmpty(array)) {
            int len = array.length;
            for (int i = beginIndex; i < len; ++i) {
                if (CharUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int indexOf(float[] array, float value, int beginIndex) {
        if (!isEmpty(array)) {
            int len = array.length;
            for (int i = beginIndex; i < len; ++i) {
                if (FloatUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int indexOf(double[] array, double value, int beginIndex) {
        if (!isEmpty(array)) {
            int len = array.length;
            for (int i = beginIndex; i < len; ++i) {
                if (DoubleUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int indexOf(byte[] array, byte value, int beginIndex) {
        if (!isEmpty(array)) {
            int len = array.length;
            for (int i = beginIndex; i < len; ++i) {
                if (ByteUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int indexOf(short[] array, short value, int beginIndex) {
        if (!isEmpty(array)) {
            int len = array.length;
            for (int i = beginIndex; i < len; ++i) {
                if (ShortUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int indexOf(int[] array, int value, int beginIndex) {
        if (!isEmpty(array)) {
            int len = array.length;
            for (int i = beginIndex; i < len; ++i) {
                if (IntUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int indexOf(long[] array, long value, int beginIndex) {
        if (!isEmpty(array)) {
            int len = array.length;
            for (int i = beginIndex; i < len; ++i) {
                if (LongUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int lastIndexOf(boolean[] array, boolean value) {
        if (!isEmpty(array)) {
            for (int i = array.length - 1; i >= 0; --i) {
                if (BooleanUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int lastIndexOf(char[] array, char value) {
        if (!isEmpty(array)) {
            for (int i = array.length - 1; i >= 0; --i) {
                if (CharUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int lastIndexOf(float[] array, float value) {
        if (!isEmpty(array)) {
            for (int i = array.length - 1; i >= 0; --i) {
                if (FloatUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int lastIndexOf(double[] array, double value) {
        if (!isEmpty(array)) {
            for (int i = array.length - 1; i >= 0; --i) {
                if (DoubleUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int lastIndexOf(byte[] array, byte value) {
        if (!isEmpty(array)) {
            for (int i = array.length - 1; i >= 0; --i) {
                if (ByteUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int lastIndexOf(short[] array, short value) {
        if (!isEmpty(array)) {
            for (int i = array.length - 1; i >= 0; --i) {
                if (ShortUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int lastIndexOf(int[] array, int value) {
        if (!isEmpty(array)) {
            for (int i = array.length - 1; i >= 0; --i) {
                if (IntUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static int lastIndexOf(long[] array, long value) {
        if (!isEmpty(array)) {
            for (int i = array.length - 1; i >= 0; --i) {
                if (LongUtils.equals(array[i], value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    static void sort(char[] a) {
        Arrays.sort(a);
    }

    static void sort(float[] a) {
        Arrays.sort(a);
    }

    static void sort(double[] a) {
        Arrays.sort(a);
    }

    static void sort(byte[] a) {
        Arrays.sort(a);
    }

    static void sort(short[] a) {
        Arrays.sort(a);
    }

    static void sort(int[] a) {
        Arrays.sort(a);
    }

    static void sort(long[] a) {
        Arrays.sort(a);
    }

    static void sort(char[] a, int fromIndex, int toIndex) {
        Arrays.sort(a, fromIndex, toIndex);
    }

    static void sort(float[] a, int fromIndex, int toIndex) {
        Arrays.sort(a, fromIndex, toIndex);
    }

    static void sort(double[] a, int fromIndex, int toIndex) {
        Arrays.sort(a, fromIndex, toIndex);
    }

    static void sort(byte[] a, int fromIndex, int toIndex) {
        Arrays.sort(a, fromIndex, toIndex);
    }

    static void sort(short[] a, int fromIndex, int toIndex) {
        Arrays.sort(a, fromIndex, toIndex);
    }

    static void sort(int[] a, int fromIndex, int toIndex) {
        Arrays.sort(a, fromIndex, toIndex);
    }

    static void sort(long[] a, int fromIndex, int toIndex) {
        Arrays.sort(a, fromIndex, toIndex);
    }

    static boolean[] newBooleanArray(int newSize) {
        return new boolean[newSize];
    }

    static char[] newCharacterArray(int newSize) {
        return new char[newSize];
    }

    static float[] newFloatArray(int newSize) {
        return new float[newSize];
    }

    static double[] newDoubleArray(int newSize) {
        return new double[newSize];
    }

    static byte[] newByteArray(int newSize) {
        return new byte[newSize];
    }

    static short[] newShortArray(int newSize) {
        return new short[newSize];
    }

    static int[] newIntegerArray(int newSize) {
        return new int[newSize];
    }

    static long[] newLongArray(int newSize) {
        return new long[newSize];
    }

    static boolean[] merge(boolean[][] arrays) {
        if (ArrayUtils.isEmpty(arrays)) {
            return new boolean[0];
        }

        if (arrays.length == 1) {
            return arrays[0];
        }

        int length = 0;
        for (boolean[] array : arrays) {
            if (!isEmpty(array)) {
                length += array.length;
            }
        }

        if (length == 0) {
            return new boolean[0];
        }

        boolean[] result = new boolean[length];
        length = 0;
        for (boolean[] array : arrays) {
            if (!isEmpty(array)) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }

        return result;
    }

    static char[] merge(char[][] arrays) {
        if (ArrayUtils.isEmpty(arrays)) {
            return new char[0];
        }

        if (arrays.length == 1) {
            return arrays[0];
        }

        int length = 0;
        for (char[] array : arrays) {
            if (!isEmpty(array)) {
                length += array.length;
            }
        }

        if (length == 0) {
            return new char[0];
        }

        char[] result = new char[length];
        length = 0;
        for (char[] array : arrays) {
            if (!isEmpty(array)) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }

        return result;
    }

    static float[] merge(float[][] arrays) {
        if (ArrayUtils.isEmpty(arrays)) {
            return new float[0];
        }

        if (arrays.length == 1) {
            return arrays[0];
        }

        int length = 0;
        for (float[] array : arrays) {
            if (!isEmpty(array)) {
                length += array.length;
            }
        }

        if (length == 0) {
            return new float[0];
        }

        float[] result = new float[length];
        length = 0;
        for (float[] array : arrays) {
            if (!isEmpty(array)) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }

        return result;
    }

    static double[] merge(double[][] arrays) {
        if (ArrayUtils.isEmpty(arrays)) {
            return new double[0];
        }

        if (arrays.length == 1) {
            return arrays[0];
        }

        int length = 0;
        for (double[] array : arrays) {
            if (!isEmpty(array)) {
                length += array.length;
            }
        }

        if (length == 0) {
            return new double[0];
        }

        double[] result = new double[length];
        length = 0;
        for (double[] array : arrays) {
            if (!isEmpty(array)) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }

        return result;
    }

    static byte[] merge(byte[][] arrays) {
        if (ArrayUtils.isEmpty(arrays)) {
            return new byte[0];
        }

        if (arrays.length == 1) {
            return arrays[0];
        }

        int length = 0;
        for (byte[] array : arrays) {
            if (!isEmpty(array)) {
                length += array.length;
            }
        }

        if (length == 0) {
            return new byte[0];
        }

        byte[] result = new byte[length];
        length = 0;
        for (byte[] array : arrays) {
            if (!isEmpty(array)) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }

        return result;
    }

    static short[] merge(short[][] arrays) {
        if (ArrayUtils.isEmpty(arrays)) {
            return new short[0];
        }

        if (arrays.length == 1) {
            return arrays[0];
        }

        int length = 0;
        for (short[] array : arrays) {
            if (!isEmpty(array)) {
                length += array.length;
            }
        }

        if (length == 0) {
            return new short[0];
        }

        short[] result = new short[length];
        length = 0;
        for (short[] array : arrays) {
            if (!isEmpty(array)) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }

        return result;
    }

    static int[] merge(int[][] arrays) {
        if (ArrayUtils.isEmpty(arrays)) {
            return new int[0];
        }

        if (arrays.length == 1) {
            return arrays[0];
        }

        int length = 0;
        for (int[] array : arrays) {
            if (!isEmpty(array)) {
                length += array.length;
            }
        }

        if (length == 0) {
            return new int[0];
        }

        int[] result = new int[length];
        length = 0;
        for (int[] array : arrays) {
            if (!isEmpty(array)) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }

        return result;
    }

    static long[] merge(long[][] arrays) {
        if (ArrayUtils.isEmpty(arrays)) {
            return new long[0];
        }

        if (arrays.length == 1) {
            return arrays[0];
        }

        int length = 0;
        for (long[] array : arrays) {
            if (!isEmpty(array)) {
                length += array.length;
            }
        }

        if (length == 0) {
            return new long[0];
        }

        long[] result = new long[length];
        length = 0;
        for (long[] array : arrays) {
            if (!isEmpty(array)) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }

        return result;
    }

    static boolean[][] split(boolean[] array, int len) {
        int amount = array.length / len;
        int remainder = array.length % len;
        if (remainder != 0) {
            ++amount;
        }

        boolean[][] arrays = new boolean[amount][];

        for (int i = 0; i < amount; ++i) {
            boolean[] arr;
            if (i == amount - 1 && remainder != 0) {
                arr = new boolean[remainder];
                System.arraycopy(array, i * len, arr, 0, remainder);
            } else {
                arr = new boolean[len];
                System.arraycopy(array, i * len, arr, 0, len);
            }

            arrays[i] = arr;
        }

        return arrays;
    }

    static char[][] split(char[] array, int len) {
        int amount = array.length / len;
        int remainder = array.length % len;
        if (remainder != 0) {
            ++amount;
        }

        char[][] arrays = new char[amount][];

        for (int i = 0; i < amount; ++i) {
            char[] arr;
            if (i == amount - 1 && remainder != 0) {
                arr = new char[remainder];
                System.arraycopy(array, i * len, arr, 0, remainder);
            } else {
                arr = new char[len];
                System.arraycopy(array, i * len, arr, 0, len);
            }

            arrays[i] = arr;
        }

        return arrays;
    }

    static float[][] split(float[] array, int len) {
        int amount = array.length / len;
        int remainder = array.length % len;
        if (remainder != 0) {
            ++amount;
        }

        float[][] arrays = new float[amount][];

        for (int i = 0; i < amount; ++i) {
            float[] arr;
            if (i == amount - 1 && remainder != 0) {
                arr = new float[remainder];
                System.arraycopy(array, i * len, arr, 0, remainder);
            } else {
                arr = new float[len];
                System.arraycopy(array, i * len, arr, 0, len);
            }

            arrays[i] = arr;
        }

        return arrays;
    }

    static double[][] split(double[] array, int len) {
        int amount = array.length / len;
        int remainder = array.length % len;
        if (remainder != 0) {
            ++amount;
        }

        double[][] arrays = new double[amount][];

        for (int i = 0; i < amount; ++i) {
            double[] arr;
            if (i == amount - 1 && remainder != 0) {
                arr = new double[remainder];
                System.arraycopy(array, i * len, arr, 0, remainder);
            } else {
                arr = new double[len];
                System.arraycopy(array, i * len, arr, 0, len);
            }

            arrays[i] = arr;
        }

        return arrays;
    }

    static byte[][] split(byte[] array, int len) {
        int amount = array.length / len;
        int remainder = array.length % len;
        if (remainder != 0) {
            ++amount;
        }

        byte[][] arrays = new byte[amount][];

        for (int i = 0; i < amount; ++i) {
            byte[] arr;
            if (i == amount - 1 && remainder != 0) {
                arr = new byte[remainder];
                System.arraycopy(array, i * len, arr, 0, remainder);
            } else {
                arr = new byte[len];
                System.arraycopy(array, i * len, arr, 0, len);
            }

            arrays[i] = arr;
        }

        return arrays;
    }

    static short[][] split(short[] array, int len) {
        int amount = array.length / len;
        int remainder = array.length % len;
        if (remainder != 0) {
            ++amount;
        }

        short[][] arrays = new short[amount][];

        for (int i = 0; i < amount; ++i) {
            short[] arr;
            if (i == amount - 1 && remainder != 0) {
                arr = new short[remainder];
                System.arraycopy(array, i * len, arr, 0, remainder);
            } else {
                arr = new short[len];
                System.arraycopy(array, i * len, arr, 0, len);
            }

            arrays[i] = arr;
        }

        return arrays;
    }

    static int[][] split(int[] array, int len) {
        int amount = array.length / len;
        int remainder = array.length % len;
        if (remainder != 0) {
            ++amount;
        }

        int[][] arrays = new int[amount][];

        for (int i = 0; i < amount; ++i) {
            int[] arr;
            if (i == amount - 1 && remainder != 0) {
                arr = new int[remainder];
                System.arraycopy(array, i * len, arr, 0, remainder);
            } else {
                arr = new int[len];
                System.arraycopy(array, i * len, arr, 0, len);
            }

            arrays[i] = arr;
        }

        return arrays;
    }

    static long[][] split(long[] array, int len) {
        int amount = array.length / len;
        int remainder = array.length % len;
        if (remainder != 0) {
            ++amount;
        }

        long[][] arrays = new long[amount][];

        for (int i = 0; i < amount; ++i) {
            long[] arr;
            if (i == amount - 1 && remainder != 0) {
                arr = new long[remainder];
                System.arraycopy(array, i * len, arr, 0, remainder);
            } else {
                arr = new long[len];
                System.arraycopy(array, i * len, arr, 0, len);
            }

            arrays[i] = arr;
        }

        return arrays;
    }

    static Boolean[] wrap(boolean[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new Boolean[0];
        }
        Boolean[] array = new Boolean[length];
        for (int i = 0; i < length; ++i) {
            array[i] = values[i];
        }
        return array;
    }

    static Character[] wrap(char[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new Character[0];
        }
        Character[] array = new Character[length];
        for (int i = 0; i < length; ++i) {
            array[i] = values[i];
        }
        return array;
    }

    static Float[] wrap(float[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new Float[0];
        }
        Float[] array = new Float[length];
        for (int i = 0; i < length; ++i) {
            array[i] = values[i];
        }
        return array;
    }

    static Double[] wrap(double[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new Double[0];
        }
        Double[] array = new Double[length];
        for (int i = 0; i < length; ++i) {
            array[i] = values[i];
        }
        return array;
    }

    static Byte[] wrap(byte[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new Byte[0];
        }
        Byte[] array = new Byte[length];
        for (int i = 0; i < length; ++i) {
            array[i] = values[i];
        }
        return array;
    }

    static Short[] wrap(short[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new Short[0];
        }
        Short[] array = new Short[length];
        for (int i = 0; i < length; ++i) {
            array[i] = values[i];
        }
        return array;
    }

    static Integer[] wrap(int[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new Integer[0];
        }
        Integer[] array = new Integer[length];
        for (int i = 0; i < length; ++i) {
            array[i] = values[i];
        }
        return array;
    }

    static Long[] wrap(long[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new Long[0];
        }
        Long[] array = new Long[length];
        for (int i = 0; i < length; ++i) {
            array[i] = values[i];
        }
        return array;
    }

    static boolean[] unWrap(Boolean[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new boolean[0];
        }
        boolean[] array = new boolean[length];
        for (int i = 0; i < length; ++i) {
            array[i] = ObjectUtils.defaultIfNull(values[i], BooleanConstants.DEFAULT);
        }
        return array;
    }

    static char[] unWrap(Character[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new char[0];
        }
        char[] array = new char[length];
        for (int i = 0; i < length; ++i) {
            array[i] = ObjectUtils.defaultIfNull(values[i], CharConstants.DEFAULT);
        }
        return array;
    }

    static float[] unWrap(Float[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new float[0];
        }
        float[] array = new float[length];
        for (int i = 0; i < length; ++i) {
            array[i] = ObjectUtils.defaultIfNull(values[i], FloatConstants.DEFAULT);
        }
        return array;
    }

    static double[] unWrap(Double[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new double[0];
        }
        double[] array = new double[length];
        for (int i = 0; i < length; ++i) {
            array[i] = ObjectUtils.defaultIfNull(values[i], DoubleConstants.DEFAULT);
        }
        return array;
    }

    static byte[] unWrap(Byte[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new byte[0];
        }
        byte[] array = new byte[length];
        for (int i = 0; i < length; ++i) {
            array[i] = ObjectUtils.defaultIfNull(values[i], ByteConstants.DEFAULT);
        }
        return array;
    }

    static short[] unWrap(Short[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new short[0];
        }
        short[] array = new short[length];
        for (int i = 0; i < length; ++i) {
            array[i] = ObjectUtils.defaultIfNull(values[i], ShortConstants.DEFAULT);
        }
        return array;
    }

    static int[] unWrap(Integer[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new int[0];
        }
        int[] array = new int[length];
        for (int i = 0; i < length; ++i) {
            array[i] = ObjectUtils.defaultIfNull(values[i], IntConstants.DEFAULT);
        }
        return array;
    }

    static long[] unWrap(Long[] values) {
        if (null == values) {
            return null;
        }
        int length = values.length;
        if (0 == length) {
            return new long[0];
        }
        long[] array = new long[length];
        for (int i = 0; i < length; ++i) {
            array[i] = ObjectUtils.defaultIfNull(values[i], LongConstants.DEFAULT);
        }
        return array;
    }

    static boolean[] sub(boolean[] array, int start, int end) {
        int length = Array.getLength(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new boolean[0];
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return new boolean[0];
            }
            end = length;
        }
        return Arrays.copyOfRange(array, start, end);
    }

    static char[] sub(char[] array, int start, int end) {
        int length = Array.getLength(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new char[0];
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return new char[0];
            }
            end = length;
        }
        return Arrays.copyOfRange(array, start, end);
    }

    static float[] sub(float[] array, int start, int end) {
        int length = Array.getLength(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new float[0];
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return new float[0];
            }
            end = length;
        }
        return Arrays.copyOfRange(array, start, end);
    }

    static double[] sub(double[] array, int start, int end) {
        int length = Array.getLength(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new double[0];
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return new double[0];
            }
            end = length;
        }
        return Arrays.copyOfRange(array, start, end);
    }

    static byte[] sub(byte[] array, int start, int end) {
        int length = Array.getLength(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new byte[0];
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return new byte[0];
            }
            end = length;
        }
        return Arrays.copyOfRange(array, start, end);
    }

    static short[] sub(short[] array, int start, int end) {
        int length = Array.getLength(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new short[0];
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return new short[0];
            }
            end = length;
        }
        return Arrays.copyOfRange(array, start, end);
    }

    static int[] sub(int[] array, int start, int end) {
        int length = Array.getLength(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new int[0];
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return new int[0];
            }
            end = length;
        }
        return Arrays.copyOfRange(array, start, end);
    }

    static long[] sub(long[] array, int start, int end) {
        int length = Array.getLength(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new long[0];
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return new long[0];
            }
            end = length;
        }
        return Arrays.copyOfRange(array, start, end);
    }

    static String join(boolean[] array, CharSequence cs) {
        if (ObjectUtils.isNull(array)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (boolean item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(cs);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    static String join(char[] array, CharSequence cs) {
        if (ObjectUtils.isNull(array)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (char item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(cs);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    static String join(float[] array, CharSequence cs) {
        if (ObjectUtils.isNull(array)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (float item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(cs);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    static String join(double[] array, CharSequence cs) {
        if (ObjectUtils.isNull(array)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (double item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(cs);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    static String join(byte[] array, CharSequence cs) {
        if (ObjectUtils.isNull(array)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (byte item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(cs);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    static String join(short[] array, CharSequence cs) {
        if (ObjectUtils.isNull(array)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (short item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(cs);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    static String join(int[] array, CharSequence cs) {
        if (ObjectUtils.isNull(array)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (int item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(cs);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    static String join(long[] array, CharSequence cs) {
        if (ObjectUtils.isNull(array)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (long item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(cs);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    static boolean[] requireNonEmpty(boolean[] array) {
        if (isEmpty(array)) {
            throw new NullPointerException();
        }
        return array;
    }

    static boolean[] requireNonEmpty(boolean[] array, String message) {
        if (isEmpty(array)) {
            throw new NullPointerException(message);
        }
        return array;
    }

    static boolean[] requireNonEmpty(boolean[] array, Supplier<String> s) {
        if (isEmpty(array)) {
            throw new NullPointerException(s.get());
        }
        return array;
    }

    static <X extends Throwable> boolean[] requireNonEmpty(boolean[] array, ThrowableSupplier<X> supplier) throws X {
        if (isEmpty(array)) {
            throw supplier.get();
        }
        return array;
    }

    static char[] requireNonEmpty(char[] array) {
        if (isEmpty(array)) {
            throw new NullPointerException();
        }
        return array;
    }

    static char[] requireNonEmpty(char[] array, String message) {
        if (isEmpty(array)) {
            throw new NullPointerException(message);
        }
        return array;
    }

    static char[] requireNonEmpty(char[] array, Supplier<String> s) {
        if (isEmpty(array)) {
            throw new NullPointerException(s.get());
        }
        return array;
    }

    static <X extends Throwable> char[] requireNonEmpty(char[] array, ThrowableSupplier<X> supplier) throws X {
        if (isEmpty(array)) {
            throw supplier.get();
        }
        return array;
    }

    static float[] requireNonEmpty(float[] array) {
        if (isEmpty(array)) {
            throw new NullPointerException();
        }
        return array;
    }

    static float[] requireNonEmpty(float[] array, String message) {
        if (isEmpty(array)) {
            throw new NullPointerException(message);
        }
        return array;
    }

    static float[] requireNonEmpty(float[] array, Supplier<String> s) {
        if (isEmpty(array)) {
            throw new NullPointerException(s.get());
        }
        return array;
    }

    static <X extends Throwable> float[] requireNonEmpty(float[] array, ThrowableSupplier<X> supplier) throws X {
        if (isEmpty(array)) {
            throw supplier.get();
        }
        return array;
    }

    static double[] requireNonEmpty(double[] array) {
        if (isEmpty(array)) {
            throw new NullPointerException();
        }
        return array;
    }

    static double[] requireNonEmpty(double[] array, String message) {
        if (isEmpty(array)) {
            throw new NullPointerException(message);
        }
        return array;
    }

    static double[] requireNonEmpty(double[] array, Supplier<String> s) {
        if (isEmpty(array)) {
            throw new NullPointerException(s.get());
        }
        return array;
    }

    static <X extends Throwable> double[] requireNonEmpty(double[] array, ThrowableSupplier<X> supplier) throws X {
        if (isEmpty(array)) {
            throw supplier.get();
        }
        return array;
    }

    static byte[] requireNonEmpty(byte[] array) {
        if (isEmpty(array)) {
            throw new NullPointerException();
        }
        return array;
    }

    static byte[] requireNonEmpty(byte[] array, String message) {
        if (isEmpty(array)) {
            throw new NullPointerException(message);
        }
        return array;
    }

    static byte[] requireNonEmpty(byte[] array, Supplier<String> s) {
        if (isEmpty(array)) {
            throw new NullPointerException(s.get());
        }
        return array;
    }

    static <X extends Throwable> byte[] requireNonEmpty(byte[] array, ThrowableSupplier<X> supplier) throws X {
        if (isEmpty(array)) {
            throw supplier.get();
        }
        return array;
    }

    static short[] requireNonEmpty(short[] array) {
        if (isEmpty(array)) {
            throw new NullPointerException();
        }
        return array;
    }

    static short[] requireNonEmpty(short[] array, String message) {
        if (isEmpty(array)) {
            throw new NullPointerException(message);
        }
        return array;
    }

    static short[] requireNonEmpty(short[] array, Supplier<String> s) {
        if (isEmpty(array)) {
            throw new NullPointerException(s.get());
        }
        return array;
    }

    static <X extends Throwable> short[] requireNonEmpty(short[] array, ThrowableSupplier<X> supplier) throws X {
        if (isEmpty(array)) {
            throw supplier.get();
        }
        return array;
    }

    static int[] requireNonEmpty(int[] array) {
        if (isEmpty(array)) {
            throw new NullPointerException();
        }
        return array;
    }

    static int[] requireNonEmpty(int[] array, String message) {
        if (isEmpty(array)) {
            throw new NullPointerException(message);
        }
        return array;
    }

    static int[] requireNonEmpty(int[] array, Supplier<String> s) {
        if (isEmpty(array)) {
            throw new NullPointerException(s.get());
        }
        return array;
    }

    static <X extends Throwable> int[] requireNonEmpty(int[] array, ThrowableSupplier<X> supplier) throws X {
        if (isEmpty(array)) {
            throw supplier.get();
        }
        return array;
    }

    static long[] requireNonEmpty(long[] array) {
        if (isEmpty(array)) {
            throw new NullPointerException();
        }
        return array;
    }

    static long[] requireNonEmpty(long[] array, String message) {
        if (isEmpty(array)) {
            throw new NullPointerException(message);
        }
        return array;
    }

    static long[] requireNonEmpty(long[] array, Supplier<String> s) {
        if (isEmpty(array)) {
            throw new NullPointerException(s.get());
        }
        return array;
    }

    static <X extends Throwable> long[] requireNonEmpty(long[] array, ThrowableSupplier<X> supplier) throws X {
        if (isEmpty(array)) {
            throw supplier.get();
        }
        return array;
    }

    static String toString(boolean[] array) {
        return Arrays.toString(array);
    }

    static String toString(char[] array) {
        return Arrays.toString(array);
    }

    static String toString(float[] array) {
        return Arrays.toString(array);
    }

    static String toString(double[] array) {
        return Arrays.toString(array);
    }

    static String toString(byte[] array) {
        return Arrays.toString(array);
    }

    static String toString(short[] array) {
        return Arrays.toString(array);
    }

    static String toString(int[] array) {
        return Arrays.toString(array);
    }

    static String toString(long[] array) {
        return Arrays.toString(array);
    }

    static boolean equals(boolean[] a1, boolean[] a2) {
        return Arrays.equals(a1, a2);
    }

    static boolean equals(char[] a1, char[] a2) {
        return Arrays.equals(a1, a2);
    }

    static boolean equals(float[] a1, float[] a2) {
        return Arrays.equals(a1, a2);
    }

    static boolean equals(double[] a1, double[] a2) {
        return Arrays.equals(a1, a2);
    }

    static boolean equals(byte[] a1, byte[] a2) {
        return Arrays.equals(a1, a2);
    }

    static boolean equals(short[] a1, short[] a2) {
        return Arrays.equals(a1, a2);
    }

    static boolean equals(int[] a1, int[] a2) {
        return Arrays.equals(a1, a2);
    }

    static boolean equals(long[] a1, long[] a2) {
        return Arrays.equals(a1, a2);
    }

    static int nullSafeHashCode(boolean[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (boolean element : array) {
            hash = 31 * hash + BooleanUtils.hashCode(element);
        }
        return hash;
    }

    static int nullSafeHashCode(char[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (char element : array) {
            hash = 31 * hash + element;
        }
        return hash;
    }

    static int nullSafeHashCode(float[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (float element : array) {
            hash = 31 * hash + FloatUtils.hashCode(element);
        }
        return hash;
    }

    static int nullSafeHashCode(double[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (double element : array) {
            hash = 31 * hash + DoubleUtils.hashCode(element);
        }
        return hash;
    }

    static int nullSafeHashCode(byte[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (byte element : array) {
            hash = 31 * hash + element;
        }
        return hash;
    }

    static int nullSafeHashCode(short[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (short element : array) {
            hash = 31 * hash + element;
        }
        return hash;
    }

    static int nullSafeHashCode(int[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (int element : array) {
            hash = 31 * hash + element;
        }
        return hash;
    }

    static int nullSafeHashCode(long[] array) {
        if (array == null) {
            return 0;
        }
        int hash = 7;
        for (long element : array) {
            hash = 31 * hash + LongUtils.hashCode(element);
        }
        return hash;
    }

    static String nullSafeToString(boolean[] array) {
        if (array == null) {
            return StringConstants.NULL;
        }
        int length = array.length;
        if (length == 0) {
            return StringConstants.LEFT_RIGHT_BRACES;
        }
        StringJoiner stringJoiner = new StringJoiner(StringConstants.COMMA_SPACE, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES);
        for (boolean b : array) {
            stringJoiner.add(String.valueOf(b));
        }
        return stringJoiner.toString();
    }

    static String nullSafeToString(char[] array) {
        if (array == null) {
            return StringConstants.NULL;
        }
        int length = array.length;
        if (length == 0) {
            return StringConstants.LEFT_RIGHT_BRACES;
        }
        StringJoiner stringJoiner = new StringJoiner(StringConstants.COMMA_SPACE, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES);
        for (char c : array) {
            stringJoiner.add(CharConstants.SINGLE_QUOTE + String.valueOf(c) + CharConstants.SINGLE_QUOTE);
        }
        return stringJoiner.toString();
    }

    static String nullSafeToString(float[] array) {
        if (array == null) {
            return StringConstants.NULL;
        }
        int length = array.length;
        if (length == 0) {
            return StringConstants.LEFT_RIGHT_BRACES;
        }
        StringJoiner stringJoiner = new StringJoiner(StringConstants.COMMA_SPACE, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES);
        for (float f : array) {
            stringJoiner.add(String.valueOf(f));
        }
        return stringJoiner.toString();
    }

    static String nullSafeToString(double[] array) {
        if (array == null) {
            return StringConstants.NULL;
        }
        int length = array.length;
        if (length == 0) {
            return StringConstants.LEFT_RIGHT_BRACES;
        }
        StringJoiner stringJoiner = new StringJoiner(StringConstants.COMMA_SPACE, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES);
        for (double d : array) {
            stringJoiner.add(String.valueOf(d));
        }
        return stringJoiner.toString();
    }

    static String nullSafeToString(byte[] array) {
        if (array == null) {
            return StringConstants.NULL;
        }
        int length = array.length;
        if (length == 0) {
            return StringConstants.LEFT_RIGHT_BRACES;
        }
        StringJoiner stringJoiner = new StringJoiner(StringConstants.COMMA_SPACE, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES);
        for (byte b : array) {
            stringJoiner.add(String.valueOf(b));
        }
        return stringJoiner.toString();
    }

    static String nullSafeToString(short[] array) {
        if (array == null) {
            return StringConstants.NULL;
        }
        int length = array.length;
        if (length == 0) {
            return StringConstants.LEFT_RIGHT_BRACES;
        }
        StringJoiner stringJoiner = new StringJoiner(StringConstants.COMMA_SPACE, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES);
        for (short s : array) {
            stringJoiner.add(String.valueOf(s));
        }
        return stringJoiner.toString();
    }

    static String nullSafeToString(int[] array) {
        if (array == null) {
            return StringConstants.NULL;
        }
        int length = array.length;
        if (length == 0) {
            return StringConstants.LEFT_RIGHT_BRACES;
        }
        StringJoiner stringJoiner = new StringJoiner(StringConstants.COMMA_SPACE, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES);
        for (int i : array) {
            stringJoiner.add(String.valueOf(i));
        }
        return stringJoiner.toString();
    }

    static String nullSafeToString(long[] array) {
        if (array == null) {
            return StringConstants.NULL;
        }
        int length = array.length;
        if (length == 0) {
            return StringConstants.LEFT_RIGHT_BRACES;
        }
        StringJoiner stringJoiner = new StringJoiner(StringConstants.COMMA_SPACE, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES);
        for (long l : array) {
            stringJoiner.add(String.valueOf(l));
        }
        return stringJoiner.toString();
    }

    static boolean[] expand(boolean[] src, int capacity) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositiveOrZero(capacity);
        if (capacity == 0) {
            return src;
        }
        boolean[] tmp = new boolean[src.length + capacity];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

    static char[] expand(char[] src, int capacity) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositiveOrZero(capacity);
        if (capacity == 0) {
            return src;
        }
        char[] tmp = new char[src.length + capacity];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

    static float[] expand(float[] src, int capacity) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositiveOrZero(capacity);
        if (capacity == 0) {
            return src;
        }
        float[] tmp = new float[src.length + capacity];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

    static double[] expand(double[] src, int capacity) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositiveOrZero(capacity);
        if (capacity == 0) {
            return src;
        }
        double[] tmp = new double[src.length + capacity];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

    static byte[] expand(byte[] src, int capacity) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositiveOrZero(capacity);
        if (capacity == 0) {
            return src;
        }
        byte[] tmp = new byte[src.length + capacity];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

    static short[] expand(short[] src, int capacity) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositiveOrZero(capacity);
        if (capacity == 0) {
            return src;
        }
        short[] tmp = new short[src.length + capacity];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

    static int[] expand(int[] src, int capacity) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositiveOrZero(capacity);
        if (capacity == 0) {
            return src;
        }
        int[] tmp = new int[src.length + capacity];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

    static long[] expand(long[] src, int capacity) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositiveOrZero(capacity);
        if (capacity == 0) {
            return src;
        }
        long[] tmp = new long[src.length + capacity];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

    static boolean[] copyOf(boolean[] src, int length) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositive(length);
        boolean[] tmp = new boolean[length];
        System.arraycopy(src, 0, tmp, 0, Math.min(src.length, length));
        return tmp;
    }

    static char[] copyOf(char[] src, int length) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositive(length);
        char[] tmp = new char[length];
        System.arraycopy(src, 0, tmp, 0, Math.min(src.length, length));
        return tmp;
    }

    static float[] copyOf(float[] src, int length) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositive(length);
        float[] tmp = new float[length];
        System.arraycopy(src, 0, tmp, 0, Math.min(src.length, length));
        return tmp;
    }

    static double[] copyOf(double[] src, int length) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositive(length);
        double[] tmp = new double[length];
        System.arraycopy(src, 0, tmp, 0, Math.min(src.length, length));
        return tmp;
    }

    static byte[] copyOf(byte[] src, int length) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositive(length);
        byte[] tmp = new byte[length];
        System.arraycopy(src, 0, tmp, 0, Math.min(src.length, length));
        return tmp;
    }

    static short[] copyOf(short[] src, int length) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositive(length);
        short[] tmp = new short[length];
        System.arraycopy(src, 0, tmp, 0, Math.min(src.length, length));
        return tmp;
    }

    static int[] copyOf(int[] src, int length) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositive(length);
        int[] tmp = new int[length];
        System.arraycopy(src, 0, tmp, 0, Math.min(src.length, length));
        return tmp;
    }

    static long[] copyOf(long[] src, int length) {
        if (src == null) {
            return null;
        }
        IntUtils.checkPositive(length);
        long[] tmp = new long[length];
        System.arraycopy(src, 0, tmp, 0, Math.min(src.length, length));
        return tmp;
    }

    static boolean[] defaultIfNull(boolean[] array) {
        return defaultIfNull(array, BooleanConstants.EMPTY_BOOLEAN_ARRAY);
    }

    static char[] defaultIfNull(char[] array) {
        return defaultIfNull(array, CharConstants.EMPTY_CHAR_ARRAY);
    }

    static float[] defaultIfNull(float[] array) {
        return defaultIfNull(array, FloatConstants.EMPTY_FLOAT_ARRAY);
    }

    static double[] defaultIfNull(double[] array) {
        return defaultIfNull(array, DoubleConstants.EMPTY_DOUBLE_ARRAY);
    }

    static byte[] defaultIfNull(byte[] array) {
        return defaultIfNull(array, ByteConstants.EMPTY_BYTE_ARRAY);
    }

    static short[] defaultIfNull(short[] array) {
        return defaultIfNull(array, ShortConstants.EMPTY_SHORT_ARRAY);
    }

    static int[] defaultIfNull(int[] array) {
        return defaultIfNull(array, IntConstants.EMPTY_INT_ARRAY);
    }

    static long[] defaultIfNull(long[] array) {
        return defaultIfNull(array, LongConstants.EMPTY_LONG_ARRAY);
    }

    static boolean[] defaultIfNull(boolean[] array, boolean[] defaultArray) {
        return ObjectUtils.defaultIfNull(array, defaultArray);
    }

    static char[] defaultIfNull(char[] array, char[] defaultArray) {
        return ObjectUtils.defaultIfNull(array, defaultArray);
    }

    static float[] defaultIfNull(float[] array, float[] defaultArray) {
        return ObjectUtils.defaultIfNull(array, defaultArray);
    }

    static double[] defaultIfNull(double[] array, double[] defaultArray) {
        return ObjectUtils.defaultIfNull(array, defaultArray);
    }

    static byte[] defaultIfNull(byte[] array, byte[] defaultArray) {
        return ObjectUtils.defaultIfNull(array, defaultArray);
    }

    static short[] defaultIfNull(short[] array, short[] defaultArray) {
        return ObjectUtils.defaultIfNull(array, defaultArray);
    }

    static int[] defaultIfNull(int[] array, int[] defaultArray) {
        return ObjectUtils.defaultIfNull(array, defaultArray);
    }

    static long[] defaultIfNull(long[] array, long[] defaultArray) {
        return ObjectUtils.defaultIfNull(array, defaultArray);
    }

    static boolean[] defaultIfEmpty(boolean[] array) {
        return defaultIfEmpty(array, BooleanConstants.EMPTY_BOOLEAN_ARRAY);
    }

    static char[] defaultIfEmpty(char[] array) {
        return defaultIfEmpty(array, CharConstants.EMPTY_CHAR_ARRAY);
    }

    static float[] defaultIfEmpty(float[] array) {
        return defaultIfEmpty(array, FloatConstants.EMPTY_FLOAT_ARRAY);
    }

    static double[] defaultIfEmpty(double[] array) {
        return defaultIfEmpty(array, DoubleConstants.EMPTY_DOUBLE_ARRAY);
    }

    static byte[] defaultIfEmpty(byte[] array) {
        return defaultIfEmpty(array, ByteConstants.EMPTY_BYTE_ARRAY);
    }

    static short[] defaultIfEmpty(short[] array) {
        return defaultIfEmpty(array, ShortConstants.EMPTY_SHORT_ARRAY);
    }

    static int[] defaultIfEmpty(int[] array) {
        return defaultIfEmpty(array, IntConstants.EMPTY_INT_ARRAY);
    }

    static long[] defaultIfEmpty(long[] array) {
        return defaultIfEmpty(array, LongConstants.EMPTY_LONG_ARRAY);
    }

    static boolean[] defaultIfEmpty(boolean[] array, boolean[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }

    static char[] defaultIfEmpty(char[] array, char[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }

    static float[] defaultIfEmpty(float[] array, float[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }

    static double[] defaultIfEmpty(double[] array, double[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }

    static byte[] defaultIfEmpty(byte[] array, byte[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }

    static short[] defaultIfEmpty(short[] array, short[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }

    static int[] defaultIfEmpty(int[] array, int[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }

    static long[] defaultIfEmpty(long[] array, long[] defaultArray) {
        return isEmpty(array) ? defaultArray : array;
    }
}