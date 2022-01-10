package com.github.bourbon.serialization.core;

import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/4 16:36
 */
public interface DataInput {

    boolean readBoolean() throws IOException;

    float readFloat() throws IOException;

    double readDouble() throws IOException;

    byte readByte() throws IOException;

    short readShort() throws IOException;

    int readInt() throws IOException;

    long readLong() throws IOException;

    String readUTF() throws IOException;

    byte[] readBytes() throws IOException;
}