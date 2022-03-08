package com.github.bourbon.pfinder.profiler.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/8 10:39
 */
public final class ByteUtils {

    private ByteUtils() {
        throw new UnsupportedOperationException();
    }

    public static byte[] intToBytes(int num) {
        return new byte[]{(byte) (num >>> 24), (byte) (num >>> 16), (byte) (num >>> 8), (byte) num};
    }

    public static int bytesToInt(byte[] ary) {
        return ary[3] & 255 | ary[2] << 8 & '\uff00' | ary[1] << 16 & 16711680 | ary[0] << 24 & -16777216;
    }

    public static byte[] shortToBytes(short num) {
        return new byte[]{(byte) (num >>> 8), (byte) num};
    }

    public static short bytesToShort(byte[] ary) {
        return (short) (ary[1] & 255 | ary[0] << 8 & '\uff00');
    }

    public static byte[] concatBytes(byte[]... arr) {
        byte[] result = new byte[Arrays.stream(arr).map(bytes -> bytes.length).reduce(Integer::sum).orElse(0)];
        int pos = 0;
        for (byte[] bytes : arr) {
            System.arraycopy(bytes, 0, result, pos, bytes.length);
            pos += bytes.length;
        }
        return result;
    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; --i) {
            result[i] = (byte) ((int) (l & 255L));
            l >>>= 8;
        }
        return result;
    }

    public static long bytesToLong(byte[] b) {
        long result = 0L;
        for (int i = 0; i < 8; ++i) {
            result <<= 8;
            result |= (b[i] & 255);
        }
        return result;
    }

    public static byte[] doubleToBytes(double value) {
        byte[] result = new byte[8];
        ByteBuffer.wrap(result).putDouble(value);
        return result;
    }

    public static double bytesToDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    public static byte[] floatToBytes(float value) {
        byte[] result = new byte[4];
        ByteBuffer.wrap(result).putFloat(value);
        return result;
    }

    public static float bytesToFloat(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getFloat();
    }

    public static byte[] subArray(byte[] b, int startIndex) {
        int len = b.length - startIndex;
        byte[] ret = new byte[len];
        System.arraycopy(b, startIndex, ret, 0, len);
        return ret;
    }

    public static byte[] subArray(byte[] b, int index, int len) {
        byte[] ret = new byte[len];
        System.arraycopy(b, index, ret, 0, len);
        return ret;
    }

    public static int readInt(byte[] b, int offset) {
        byte[] tmp = new byte[4];
        for (int index = 0; index != 4; ++index) {
            tmp[index] = b[offset + index];
        }
        return bytesToInt(tmp);
    }

    public static byte readByte(InputStream is) throws IOException {
        return readBytes(is, 1)[0];
    }

    public static short readShort(InputStream is) throws IOException {
        return bytesToShort(readBytes(is, 2));
    }

    public static int readInt(InputStream is) throws IOException {
        return bytesToInt(readBytes(is, 4));
    }

    public static long readLong(InputStream is) throws IOException {
        return bytesToLong(readBytes(is, 8));
    }

    public static double readDouble(InputStream is) throws IOException {
        return bytesToDouble(readBytes(is, 8));
    }

    public static float readFloat(InputStream is) throws IOException {
        return bytesToFloat(readBytes(is, 4));
    }

    public static byte[] readBytes(InputStream is, int length) throws IOException {
        if (is.available() < length) {
            throw new IOException("Insufficient available length, remaining=" + is.available() + " expect=" + length);
        }
        byte[] result = new byte[length];
        is.read(result);
        return result;
    }

    public static long readLong(byte[] b, int offset) {
        byte[] tmp = new byte[8];
        for (int index = 0; index != 8; ++index) {
            tmp[index] = b[offset + index];
        }
        return bytesToLong(tmp);
    }

    public static boolean compare(byte[] a, byte[] b) {
        if (a != null && b != null && a.length == b.length) {
            for (int i = 0, len = a.length; i < len; ++i) {
                if (a[i] != b[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static void writeShort(short value, OutputStream os) throws IOException {
        os.write(shortToBytes(value));
    }

    public static void writeInt(int value, OutputStream os) throws IOException {
        os.write(intToBytes(value));
    }

    public static void writeLong(long value, OutputStream os) throws IOException {
        os.write(longToBytes(value));
    }

    public static void writeFloat(float value, OutputStream os) throws IOException {
        os.write(floatToBytes(value));
    }

    public static void writeDouble(double value, OutputStream os) throws IOException {
        os.write(doubleToBytes(value));
    }

    public static int hash(byte[] data) {
        if (data != null && data.length != 0) {
            int p = 16777619;
            int hash = -2128831035;
            for (byte datum : data) {
                hash = (hash ^ datum) * p;
            }
            hash += hash << 13;
            hash ^= hash >> 7;
            hash += hash << 3;
            hash ^= hash >> 17;
            hash += hash << 5;
            return hash < 0 ? hash * -1 : hash;
        }
        return 0;
    }
}