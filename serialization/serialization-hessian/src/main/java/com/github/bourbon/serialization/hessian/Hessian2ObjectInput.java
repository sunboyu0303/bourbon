package com.github.bourbon.serialization.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.github.bourbon.base.lang.clean.Cleanable;
import com.github.bourbon.serialization.core.ObjectInput;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 14:12
 */
class Hessian2ObjectInput implements ObjectInput, Cleanable {

    private final Hessian2Input input;

    Hessian2ObjectInput(InputStream input) {
        this.input = new Hessian2Input(input);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls) throws IOException {
        return (T) input.readObject(cls);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return input.readBoolean();
    }

    @Override
    public float readFloat() throws IOException {
        return (float) input.readDouble();
    }

    @Override
    public double readDouble() throws IOException {
        return input.readDouble();
    }

    @Override
    public byte readByte() throws IOException {
        return (byte) input.readInt();
    }

    @Override
    public short readShort() throws IOException {
        return (short) input.readInt();
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
        return input.readString();
    }

    @Override
    public byte[] readBytes() throws IOException {
        return input.readBytes();
    }

    @Override
    public void cleanup() {
        input.reset();
    }
}