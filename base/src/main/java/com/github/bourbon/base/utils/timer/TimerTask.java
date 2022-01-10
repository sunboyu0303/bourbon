package com.github.bourbon.base.utils.timer;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/12 09:03
 */
public interface TimerTask {
    
    void run(Timeout timeout) throws Exception;
}