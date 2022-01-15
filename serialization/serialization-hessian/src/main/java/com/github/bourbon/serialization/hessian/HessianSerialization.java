package com.github.bourbon.serialization.hessian;

import com.github.bourbon.serialization.core.ObjectInput;
import com.github.bourbon.serialization.core.ObjectOutput;
import com.github.bourbon.serialization.core.Serialization;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 14:11
 */
public class HessianSerialization implements Serialization {

    @Override
    public ObjectOutput serialize(OutputStream output) {
        return new HessianObjectOutput(output);
    }

    @Override
    public ObjectInput deserialize(InputStream input) {
        return new HessianObjectInput(input);
    }
}