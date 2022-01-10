package com.github.bourbon.bytecode.core.constant;

import com.github.bourbon.base.constant.StringConstants;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 16:50
 */
public final class ByteCodeConstants {

    public static final String PROXY = "Proxy";
    /**
     * {@code "Proxy.class"}
     */
    public static final String PROXY_CLASS = PROXY + StringConstants.CLASS_FILE_SUFFIX;

    private ByteCodeConstants() {
    }
}