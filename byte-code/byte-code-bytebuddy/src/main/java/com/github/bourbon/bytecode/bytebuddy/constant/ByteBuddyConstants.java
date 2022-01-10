package com.github.bourbon.bytecode.bytebuddy.constant;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.bytecode.core.constant.ByteCodeConstants;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 23:12
 */
public final class ByteBuddyConstants {

    public static final String BYTE_BUDDY = "ByteBuddy";
    /**
     * {@code "$$Proxy"}
     */
    public static final String DOLLARS_PROXY = StringConstants.DOLLARS + ByteCodeConstants.PROXY;
    /**
     * {@code "$$ByteBuddy"}
     */
    public static final String DOLLARS_BYTE_BUDDY = StringConstants.DOLLARS + BYTE_BUDDY;

    private ByteBuddyConstants() {
    }
}