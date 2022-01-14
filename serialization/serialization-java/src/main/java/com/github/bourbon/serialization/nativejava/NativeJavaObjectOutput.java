package com.github.bourbon.serialization.nativejava;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.serialization.core.ObjectOutput;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 10:38
 */
public class NativeJavaObjectOutput implements ObjectOutput {

    private final ObjectOutputStream output;

    NativeJavaObjectOutput(OutputStream output) throws IOException {
        this(new ObjectOutputStream(output));
    }

    protected NativeJavaObjectOutput(ObjectOutputStream output) throws IOException {
        Assert.notNull(output, "output == null");
        this.output = new ObjectOutputStream(output);
    }

    protected ObjectOutputStream getObjectOutputStream() {
        return output;
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        output.writeObject(obj);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        output.writeBoolean(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        output.writeFloat(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        output.writeDouble(v);
    }

    @Override
    public void writeByte(byte v) throws IOException {
        output.writeByte(v);
    }

    @Override
    public void writeShort(short v) throws IOException {
        output.writeShort(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        output.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        output.writeLong(v);
    }

    @Override
    public void writeUTF(String v) throws IOException {
        output.writeUTF(v);
    }

    @Override
    public void writeBytes(byte[] v) throws IOException {
        if (v == null) {
            output.writeInt(-1);
        } else {
            writeBytes(v, 0, v.length);
        }
    }

    @Override
    public void writeBytes(byte[] v, int off, int len) throws IOException {
        if (v == null) {
            output.writeInt(-1);
        } else {
            output.writeInt(len);
            output.write(v, off, len);
        }
    }

    @Override
    public void flushBuffer() throws IOException {
        output.flush();
    }
}