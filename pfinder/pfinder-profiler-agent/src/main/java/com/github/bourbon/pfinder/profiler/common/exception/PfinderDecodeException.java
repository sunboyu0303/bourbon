package com.github.bourbon.pfinder.profiler.common.exception;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 16:35
 */
public class PfinderDecodeException extends PfinderException {

    public PfinderDecodeException() {
    }

    public PfinderDecodeException(String message) {
        super(message);
    }

    public PfinderDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PfinderDecodeException(Throwable cause) {
        super(cause);
    }
}