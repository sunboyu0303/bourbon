package com.github.bourbon.pfinder.profiler.exception;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 15:12
 */
public class PfinderAddonException extends PfinderException {
    
    public PfinderAddonException() {
    }

    public PfinderAddonException(String message) {
        super(message);
    }

    public PfinderAddonException(String message, Throwable cause) {
        super(message, cause);
    }

    public PfinderAddonException(Throwable cause) {
        super(cause);
    }
}