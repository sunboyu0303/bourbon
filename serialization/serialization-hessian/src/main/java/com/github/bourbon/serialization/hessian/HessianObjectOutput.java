package com.github.bourbon.serialization.hessian;

import com.caucho.hessian.io.HessianOutput;
import com.github.bourbon.serialization.core.Cleanable;
import com.github.bourbon.serialization.core.ObjectOutput;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 14:19
 */
class HessianObjectOutput implements ObjectOutput, Cleanable {

    private final HessianOutput output;

    HessianObjectOutput(OutputStream output) {
        this.output = new HessianOutput(output);
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
        output.writeDouble(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        output.writeDouble(v);
    }

    @Override
    public void writeByte(byte v) throws IOException {
        output.writeInt(v);
    }

    @Override
    public void writeShort(short v) throws IOException {
        output.writeInt(v);
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
        output.writeString(v);
    }

    @Override
    public void writeBytes(byte[] v) throws IOException {
        output.writeBytes(v);
    }

    @Override
    public void writeBytes(byte[] v, int off, int len) throws IOException {
        output.writeBytes(v, off, len);
    }

    @Override
    public void flushBuffer() throws IOException {
        output.flush();
    }

    @Override
    public void cleanup() {
        output.resetReferences();
    }
}