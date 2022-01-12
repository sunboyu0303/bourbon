package com.github.bourbon.base.io.support;

import com.github.bourbon.base.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 11:44
 */
class FileSystemResource implements Resource {

    private final File file;

    FileSystemResource(File file) {
        this.file = file;
    }

    FileSystemResource(String path) {
        this(new File(path));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }
}