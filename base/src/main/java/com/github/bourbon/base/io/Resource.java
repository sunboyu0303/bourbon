package com.github.bourbon.base.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 11:36
 */
public interface Resource {

    InputStream getInputStream() throws IOException;
}