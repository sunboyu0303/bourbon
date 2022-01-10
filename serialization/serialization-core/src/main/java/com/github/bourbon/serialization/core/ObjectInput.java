package com.github.bourbon.serialization.core;

import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/4 16:39
 */
public interface ObjectInput extends DataInput {

    <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException;
}