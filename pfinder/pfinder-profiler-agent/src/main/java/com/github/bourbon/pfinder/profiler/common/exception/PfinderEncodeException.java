package com.github.bourbon.pfinder.profiler.common.exception;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 16:36
 */
public class PfinderEncodeException extends PfinderException {

    public PfinderEncodeException() {
    }

    public PfinderEncodeException(String message) {
        super(message);
    }

    public PfinderEncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PfinderEncodeException(Throwable cause) {
        super(cause);
    }
}