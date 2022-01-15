package com.github.bourbon.serialization.java;

import com.github.bourbon.serialization.core.ObjectInput;
import com.github.bourbon.serialization.core.ObjectOutput;
import com.github.bourbon.serialization.core.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 09:54
 */
public class JavaSerialization implements Serialization {

    @Override
    public ObjectOutput serialize(OutputStream output) throws IOException {
        return new JavaObjectOutput(output);
    }

    @Override
    public ObjectInput deserialize(InputStream input) throws IOException {
        return new JavaObjectInput(input);
    }
}