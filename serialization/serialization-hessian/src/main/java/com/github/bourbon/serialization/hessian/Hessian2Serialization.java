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
public class Hessian2Serialization implements Serialization {

    @Override
    public ObjectOutput serialize(OutputStream output) {
        return new Hessian2ObjectOutput(output);
    }

    @Override
    public ObjectInput deserialize(InputStream input) {
        return new Hessian2ObjectInput(input);
    }
}