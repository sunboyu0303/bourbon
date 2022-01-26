package com.github.bourbon.tracer.core.span;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 13:50
 */
public final class LogData {

    public static final String EVENT_TYPE_KEY = "event";

    public static final String CLIENT_RECEIVE_EVENT_VALUE = "cr";

    public static final String CLIENT_SEND_EVENT_VALUE = "cs";

    public static final String SERVER_RECEIVE_EVENT_VALUE = "sr";

    public static final String SERVER_SEND_EVENT_VALUE = "ss";

    private final long time;

    private final Map<String, ?> fields;

    public LogData(long time, Map<String, ?> fields) {
        this.time = time;
        this.fields = fields;
    }

    public long getTime() {
        return time;
    }

    public Map<String, ?> getFields() {
        return fields;
    }
}