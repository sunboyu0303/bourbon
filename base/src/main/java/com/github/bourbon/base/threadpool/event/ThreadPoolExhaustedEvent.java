package com.github.bourbon.base.threadpool.event;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 16:32
 */
public class ThreadPoolExhaustedEvent {

    private final String msg;

    public ThreadPoolExhaustedEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}