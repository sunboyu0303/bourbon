package com.github.bourbon.serialization.java;

import com.github.bourbon.base.utils.ClassLoaderUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.io.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/5 11:27
 */
class CompactedObjectInputStream extends ObjectInputStream {

    private ClassLoader classLoader;

    CompactedObjectInputStream(InputStream input) throws IOException {
        this(input, Thread.currentThread().getContextClassLoader());
    }

    CompactedObjectInputStream(InputStream input, ClassLoader cl) throws IOException {
        super(input);
        classLoader = ObjectUtils.defaultSupplierIfNull(cl, ClassLoaderUtils::getClassLoader);
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        int type = read();
        if (type < 0) {
            throw new EOFException();
        }
        switch (type) {
            case 0:
                return super.readClassDescriptor();
            case 1:
                return ObjectStreamClass.lookup(classLoader.loadClass(readUTF()));
            default:
                throw new StreamCorruptedException("Unexpected class descriptor type: " + type);
        }
    }
}