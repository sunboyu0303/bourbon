package com.github.bourbon.serialization.java;

import com.github.bourbon.base.constant.ByteConstants;
import com.github.bourbon.base.constant.IntConstants;
import com.github.bourbon.serialization.nativejava.NativeJavaObjectInput;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 11:00
 */
class JavaObjectInput extends NativeJavaObjectInput {

    JavaObjectInput(InputStream input) throws IOException {
        super(new ObjectInputStream(input));
    }

    JavaObjectInput(InputStream input, boolean compacted) throws IOException {
        super(compacted ? new CompactedObjectInputStream(input) : new ObjectInputStream(input));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return getObjectInputStream().readByte() == 0 ? null : (T) getObjectInputStream().readObject();
    }

    @Override
    public String readUTF() throws IOException {
        return getObjectInputStream().readInt() < 0 ? null : getObjectInputStream().readUTF();
    }

    @Override
    public byte[] readBytes() throws IOException {
        int len = getObjectInputStream().readInt();
        if (len < 0) {
            return null;
        }
        if (len == 0) {
            return ByteConstants.EMPTY_BYTE_ARRAY;
        }
        if (len > IntConstants.MB8) {
            throw new IOException("Byte array length too large. " + len);
        }
        byte[] b = new byte[len];
        getObjectInputStream().readFully(b);
        return b;
    }
}