package com.github.bourbon.springframework.beans;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 14:32
 */
public class BeansException extends RuntimeException {

    private static final long serialVersionUID = -4959177947143577511L;

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}