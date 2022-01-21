package com.github.bourbon.ump.domain;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/25 10:07
 */
public enum LogType {
    TP((byte) 0, "-tp"),
    JVM((byte) 1, "-jvm"),
    ALIVE((byte) 2, "-alive");

    private final byte value;
    private final String type;

    LogType(byte value, String type) {
        this.value = value;
        this.type = type;
    }

    public byte getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}