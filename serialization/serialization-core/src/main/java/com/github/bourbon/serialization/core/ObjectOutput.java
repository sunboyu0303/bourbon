package com.github.bourbon.serialization.core;

import java.io.IOException;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/4 16:42
 */
public interface ObjectOutput extends DataOutput {

    void writeObject(Object obj) throws IOException;
}