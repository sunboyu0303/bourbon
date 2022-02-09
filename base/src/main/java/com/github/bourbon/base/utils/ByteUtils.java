package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.ByteConstants;
import com.github.bourbon.base.utils.function.ThrowableSupplier;

import java.nio.ByteOrder;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 09:52
 */
public final class ByteUtils {

    private static final Set<Class<?>> CLASSES = SetUtils.newHashSet(ByteConstants.PRIMITIVE_CLASS, ByteConstants.BOXED_CLASS);

    public static byte checkPositive(byte b) {
        if (b <= ByteConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return b;
    }

    public static byte checkPositive(byte b, String name) {
        if (b <= ByteConstants.DEFAULT) {
            throw new IllegalArgumentException(name + " : " + b + " (expected: > 0)");
        }
        return b;
    }

    public static byte checkPositive(byte b, Supplier<String> s) {
        if (b <= ByteConstants.DEFAULT) {
            throw new IllegalArgumentException(s.get());
        }
        return b;
    }

    public static <X extends Throwable> byte checkPositive(byte b, ThrowableSupplier<X> s) throws X {
        if (b <= ByteConstants.DEFAULT) {
            throw s.get();
        }
        return b;
    }

    public static byte checkPositiveOrZero(byte b) {
        if (b < ByteConstants.DEFAULT) {
            throw new IllegalArgumentException();
        }
        return b;
    }

    public static byte checkPositiveOrZero(byte b, String name) {
        if (b < ByteConstants.DEFAULT) {
            throw new IllegalArgumentException(name + " : " + b + " (expected: >= 0)");
        }
        return b;
    }

    public static byte checkPositiveOrZero(byte b, Supplier<String> s) {
        if (b < ByteConstants.DEFAULT) {
            throw new IllegalArgumentException(s.get());
        }
        return b;
    }

    public static <X extends Throwable> byte checkPositiveOrZero(byte b, ThrowableSupplier<X> s) throws X {
        if (b < ByteConstants.DEFAULT) {
            throw s.get();
        }
        return b;
    }

    public static byte checkInRange(byte b, byte start, byte end) {
        if (b >= start && b <= end) {
            return b;
        }
        throw new IllegalArgumentException();
    }

    public static byte checkInRange(byte b, byte start, byte end, String name) {
        if (b >= start && b <= end) {
            return b;
        }
        throw new IllegalArgumentException(name + ": " + b + " (expected: " + start + "-" + end + ")");
    }

    public static byte checkInRange(byte b, byte start, byte end, Supplier<String> s) {
        if (b >= start && b <= end) {
            return b;
        }
        throw new IllegalArgumentException(s.get());
    }

    public static <X extends Throwable> byte checkInRange(byte b, byte start, byte end, ThrowableSupplier<X> s) throws X {
        if (b >= start && b <= end) {
            return b;
        }
        throw s.get();
    }

    public static boolean isByte(Class<?> clazz) {
        return CLASSES.contains(clazz);
    }

    public static byte byteValue(Byte wrapper, byte defaultValue) {
        return ObjectUtils.defaultIfNull(wrapper, defaultValue);
    }

    public static boolean equals(byte b1, byte b2) {
        return b1 == b2;
    }

    public static short bytesToShort(byte[] bytes) {
        return bytesToShort(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static short bytesToShort(byte[] bytes, ByteOrder byteOrder) {
        return BooleanUtils.defaultSupplierIfFalse(ByteOrder.LITTLE_ENDIAN == byteOrder, () -> (short) (bytes[0] & 255 | (bytes[1] & 255) << 8), () -> (short) (bytes[1] & 255 | (bytes[0] & 255) << 8));
    }

    public static byte[] shortToBytes(short v) {
        return shortToBytes(v, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] shortToBytes(short v, ByteOrder byteOrder) {
        return BooleanUtils.defaultSupplierIfFalse(ByteOrder.LITTLE_ENDIAN == byteOrder, () -> new byte[]{(byte) v, (byte) (v >>> 8)}, () -> new byte[]{(byte) (v >>> 8), (byte) v});
    }

    public static int bytesToInt(byte[] bytes) {
        return bytesToInt(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static int bytesToInt(byte[] bytes, ByteOrder byteOrder) {
        return BooleanUtils.defaultSupplierIfFalse(ByteOrder.LITTLE_ENDIAN == byteOrder, () -> bytes[0] & 255 | (bytes[1] & 255) << 8 | (bytes[2] & 255) << 16 | (bytes[3] & 255) << 24, () -> bytes[3] & 255 | (bytes[2] & 255) << 8 | (bytes[1] & 255) << 16 | (bytes[0] & 255) << 24);
    }

    public static byte[] intToBytes(int v) {
        return intToBytes(v, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] intToBytes(int v, ByteOrder byteOrder) {
        return BooleanUtils.defaultSupplierIfFalse(ByteOrder.LITTLE_ENDIAN == byteOrder, () -> new byte[]{(byte) v, (byte) (v >>> 8), (byte) (v >>> 16), (byte) (v >>> 24)}, () -> new byte[]{(byte) (v >>> 24), (byte) (v >>> 16), (byte) (v >>> 8), (byte) v});
    }

    public static long bytesToLong(byte[] bytes) {
        return bytesToLong(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static long bytesToLong(byte[] bytes, ByteOrder byteOrder) {
        return BooleanUtils.defaultSupplierIfFalse(ByteOrder.LITTLE_ENDIAN == byteOrder,
                () -> ((long) (bytes[0] & 255)) | ((long) (bytes[1] & 255)) << 8 | ((long) (bytes[2] & 255)) << 16 | ((long) (bytes[3] & 255)) << 24 | ((long) (bytes[4] & 255)) << 32 | ((long) (bytes[5] & 255)) << 40 | ((long) (bytes[6] & 255)) << 48 | ((long) (bytes[7] & 255)) << 56,
                () -> ((long) (bytes[7] & 255)) | (((long) bytes[6] & 255)) << 8 | ((long) (bytes[5] & 255)) << 16 | ((long) (bytes[4] & 255)) << 24 | ((long) (bytes[3] & 255)) << 32 | ((long) (bytes[2] & 255)) << 40 | ((long) (bytes[1] & 255)) << 48 | ((long) (bytes[0] & 255)) << 56
        );
    }

    public static byte[] longToBytes(long v) {
        return longToBytes(v, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] longToBytes(long v, ByteOrder byteOrder) {
        return BooleanUtils.defaultSupplierIfFalse(ByteOrder.LITTLE_ENDIAN == byteOrder,
                () -> new byte[]{(byte) v, (byte) (v >>> 8), (byte) (v >>> 16), (byte) (v >>> 24), (byte) (v >>> 32), (byte) (v >>> 40), (byte) (v >>> 48), (byte) (v >>> 56)},
                () -> new byte[]{(byte) (v >>> 56), (byte) (v >>> 48), (byte) (v >>> 40), (byte) (v >>> 32), (byte) (v >>> 24), (byte) (v >>> 16), (byte) (v >>> 8), (byte) v}
        );
    }

    public static float bytesToFloat(byte[] bytes) {
        return bytesToFloat(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static float bytesToFloat(byte[] bytes, ByteOrder byteOrder) {
        return Float.intBitsToFloat(bytesToInt(bytes, byteOrder));
    }

    public static byte[] floatToBytes(float v) {
        return floatToBytes(v, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] floatToBytes(float v, ByteOrder byteOrder) {
        return intToBytes(Float.floatToIntBits(v), byteOrder);
    }

    public static double bytesToDouble(byte[] bytes) {
        return bytesToDouble(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static double bytesToDouble(byte[] bytes, ByteOrder byteOrder) {
        return Double.longBitsToDouble(bytesToLong(bytes, byteOrder));
    }

    public static byte[] doubleToBytes(double v) {
        return doubleToBytes(v, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] doubleToBytes(double v, ByteOrder byteOrder) {
        return longToBytes(Double.doubleToLongBits(v), byteOrder);
    }

    public static int indexOf(byte[] org, byte[] search) {
        return indexOf(org, search, 0);
    }

    public static int indexOf(byte[] org, byte[] search, int startIndex) {
        KMPMatcher kmpMatcher = new KMPMatcher();
        kmpMatcher.computeFailure4Byte(search);
        return kmpMatcher.indexOf(org, startIndex);
    }

    private static class KMPMatcher {

        private int[] failure;
        private int matchPoint;
        private byte[] bytePattern;

        private int indexOf(byte[] text, int startIndex) {
            int j = 0;
            if (text.length == 0 || startIndex > text.length) {
                return -1;
            }
            for (int i = startIndex; i < text.length; i++) {
                while (j > 0 && bytePattern[j] != text[i]) {
                    j = failure[j - 1];
                }
                if (bytePattern[j] == text[i]) {
                    j++;
                }
                if (j == bytePattern.length) {
                    matchPoint = i - bytePattern.length + 1;
                    return matchPoint;
                }
            }
            return -1;

        }

        private int lastIndexOf(byte[] text, int startIndex) {
            matchPoint = -1;
            int j = 0;
            if (text.length == 0 || startIndex > text.length) {
                return -1;
            }
            int end = text.length;
            for (int i = startIndex; i < end; i++) {
                while (j > 0 && bytePattern[j] != text[i]) {
                    j = failure[j - 1];
                }
                if (bytePattern[j] == text[i]) {
                    j++;
                }
                if (j == bytePattern.length) {
                    matchPoint = i - bytePattern.length + 1;
                    if ((text.length - i) > bytePattern.length) {
                        j = 0;
                        continue;
                    }
                    return matchPoint;
                }
                // If you find it from a position in the middle, if you don’t find it at the end, then start looking for it again.
                if (startIndex != 0 && i + 1 == end) {
                    end = startIndex;
                    i = -1;
                    startIndex = 0;
                }
            }
            return matchPoint;
        }

        private int lastIndexOfWithNoLoop(byte[] text, int startIndex) {
            matchPoint = -1;
            int j = 0;
            if (text.length == 0 || startIndex > text.length) {
                return -1;
            }
            for (int i = startIndex; i < text.length; i++) {
                while (j > 0 && bytePattern[j] != text[i]) {
                    j = failure[j - 1];
                }
                if (bytePattern[j] == text[i]) {
                    j++;
                }
                if (j == bytePattern.length) {
                    matchPoint = i - bytePattern.length + 1;
                    if ((text.length - i) > bytePattern.length) {
                        j = 0;
                        continue;
                    }
                    return matchPoint;
                }
            }
            return matchPoint;
        }

        private void computeFailure4Byte(byte[] patternStr) {
            bytePattern = patternStr;
            int j = 0;
            int len = bytePattern.length;
            failure = new int[len];
            for (int i = 1; i < len; i++) {
                while (j > 0 && bytePattern[j] != bytePattern[i]) {
                    j = failure[j - 1];
                }
                if (bytePattern[j] == bytePattern[i]) {
                    j++;
                }
                failure[i] = j;
            }
        }
    }

    private ByteUtils() {
    }
}