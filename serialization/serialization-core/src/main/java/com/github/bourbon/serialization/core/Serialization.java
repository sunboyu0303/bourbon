package com.github.bourbon.serialization.core;

import com.github.bourbon.base.extension.annotation.ExtensionScope;
import com.github.bourbon.base.extension.annotation.SPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/4 17:28
 */
@SPI(scope = ExtensionScope.FRAMEWORK)
public interface Serialization {

    ObjectOutput serialize(OutputStream output) throws IOException;

    ObjectInput deserialize(InputStream input) throws IOException;
}