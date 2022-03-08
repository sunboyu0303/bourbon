package com.github.bourbon.pfinder.profiler.common.exception;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 15:28
 */
public class PfinderException extends Exception {

    public PfinderException() {
    }

    public PfinderException(String message) {
        super(message);
    }

    public PfinderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PfinderException(Throwable cause) {
        super(cause);
    }
}