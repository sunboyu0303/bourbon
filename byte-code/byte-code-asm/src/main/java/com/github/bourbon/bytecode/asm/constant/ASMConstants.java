package com.github.bourbon.bytecode.asm.constant;

import com.github.bourbon.base.constant.StringConstants;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 18:48
 */
public final class ASMConstants {

    public static final String TARGET = "target";

    public static final String SET_TARGET = "setTarget";

    public static final String GET_TARGET = "getTarget";

    public static final String THIS = "this";

    public static final String INIT = "<init>";

    public static final String START = "start";

    public static final String END = "end";
    /**
     * {@code "()V"}
     */
    public static final String LEFT_RIGHT_PARENTHESES_V = StringConstants.LEFT_RIGHT_PARENTHESES + StringConstants.V;
    /**
     * {@code ")V"}
     */
    public static final String RIGHT_PARENTHESES_V = StringConstants.RIGHT_PARENTHESES + StringConstants.V;

    private ASMConstants() {
    }
}