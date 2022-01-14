package com.github.bourbon.serialization.java;

import com.github.bourbon.serialization.nativejava.NativeJavaObjectOutput;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 11:09
 */
class JavaObjectOutput extends NativeJavaObjectOutput {

    JavaObjectOutput(OutputStream output) throws IOException {
        super(new ObjectOutputStream(output));
    }

    JavaObjectOutput(OutputStream output, boolean compact) throws IOException {
        super(compact ? new CompactedObjectOutputStream(output) : new ObjectOutputStream(output));
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        if (obj == null) {
            getObjectOutputStream().writeByte(0);
        } else {
            getObjectOutputStream().writeByte(1);
            getObjectOutputStream().writeObject(obj);
        }
    }

    @Override
    public void writeUTF(String v) throws IOException {
        if (v == null) {
            getObjectOutputStream().writeInt(-1);
        } else {
            getObjectOutputStream().writeInt(v.length());
            getObjectOutputStream().writeUTF(v);
        }
    }

    @Override
    public void flushBuffer() throws IOException {
        getObjectOutputStream().flush();
    }
}