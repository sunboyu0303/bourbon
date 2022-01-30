package com.github.bourbon.base.utils;

import com.github.bourbon.base.disruptor.lang.Sequence;
import com.github.bourbon.base.disruptor.processor.EventProcessor;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/4 17:10
 */
public final class Utils {

    public static int ceilingNextPowerOfTwo(int x) {
        return 1 << (32 - Integer.numberOfLeadingZeros(x - 1));
    }

    public static long getMinimumSequence(Sequence[] sequences) {
        return getMinimumSequence(sequences, Long.MAX_VALUE);
    }

    public static long getMinimumSequence(Sequence[] sequences, long minimum) {
        for (Sequence sequence : sequences) {
            minimum = Math.min(minimum, sequence.get());
        }
        return minimum;
    }

    public static Sequence[] getSequencesFor(EventProcessor... processors) {
        Sequence[] sequences = new Sequence[processors.length];
        for (int i = 0; i < sequences.length; i++) {
            sequences[i] = processors[i].getSequence();
        }
        return sequences;
    }

    private static final Unsafe THE_UNSAFE;

    static {
        try {
            PrivilegedExceptionAction<Unsafe> action = () -> {
                Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                return (Unsafe) theUnsafe.get(null);
            };
            THE_UNSAFE = AccessController.doPrivileged(action);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }

    public static Unsafe getUnsafe() {
        return THE_UNSAFE;
    }

    public static long getAddressFromDirectByteBuffer(ByteBuffer buffer) {
        try {
            Field addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            return addressField.getLong(buffer);
        } catch (Exception e) {
            throw new RuntimeException("Unable to address field from ByteBuffer", e);
        }
    }

    public static int log2(int i) {
        int r = 0;
        while ((i >>= 1) != 0) {
            ++r;
        }
        return r;
    }

    private Utils() {
    }
}