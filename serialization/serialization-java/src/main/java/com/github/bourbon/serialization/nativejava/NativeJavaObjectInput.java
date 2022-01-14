package com.github.bourbon.serialization.nativejava;

import com.github.bourbon.base.constant.ByteConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.serialization.core.ObjectInput;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 10:30
 */
public class NativeJavaObjectInput implements ObjectInput {

    private final ObjectInputStream input;

    NativeJavaObjectInput(InputStream input) throws IOException {
        this(new ObjectInputStream(input));
    }

    protected NativeJavaObjectInput(ObjectInputStream input) throws IOException {
        Assert.notNull(input, "input == null");
        this.input = new ObjectInputStream(input);
    }

    protected ObjectInputStream getObjectInputStream() {
        return input;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        return (T) input.readObject();
    }

    @Override
    public boolean readBoolean() throws IOException {
        return input.readBoolean();
    }

    @Override
    public float readFloat() throws IOException {
        return input.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return input.readDouble();
    }

    @Override
    public byte readByte() throws IOException {
        return input.readByte();
    }

    @Override
    public short readShort() throws IOException {
        return input.readShort();
    }

    @Override
    public int readInt() throws IOException {
        return input.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return input.readLong();
    }

    @Override
    public String readUTF() throws IOException {
        return input.readUTF();
    }

    @Override
    public byte[] readBytes() throws IOException {
        int len = input.readInt();
        if (len < 0) {
            return null;
        }
        if (len == 0) {
            return ByteConstants.EMPTY_BYTE_ARRAY;
        }
        byte[] result = new byte[len];
        input.readFully(result);
        return result;
    }
}