package com.github.bourbon.ump.domain;

import com.alibaba.fastjson.JSON;
import com.github.bourbon.base.lang.SystemClock;
import com.github.bourbon.base.utils.LocalDateTimeUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/24 18:26
 */
public class CallerInfo {

    public static final int STATE_TRUE = 0;
    public static final int STATE_FALSE = 1;

    private final String key;
    private final long startTime = SystemClock.currentTimeMillis();
    private long elapsedTime = -1L;
    private int processState = STATE_TRUE;

    public CallerInfo(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public int getProcessState() {
        return processState;
    }

    public void error() {
        processState = STATE_FALSE;
    }

    public long getElapsedTime() {
        return elapsedTime == -1L ? calcElapsedTime() : elapsedTime;
    }

    private long calcElapsedTime() {
        elapsedTime = SystemClock.currentTimeMillis() - startTime;
        return elapsedTime;
    }

    public String getTime() {
        return LocalDateTimeUtils.localDateTimeNowFormat();
    }
}