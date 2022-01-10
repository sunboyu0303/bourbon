package com.github.bourbon.base.utils.timer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 09:02
 */
public interface Timeout {

    Timer timer();

    TimerTask task();

    boolean isExpired();

    boolean isCancelled();

    boolean cancel();
}