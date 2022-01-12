package com.github.bourbon.base.logger;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/14 10:06
 */
public final class LogInfo {
    private String eventName;
    private String traceId;
    private String msg;
    private long costTime;
    private Object[] request;
    private Object response;
    private Object others;

    public String getEventName() {
        return eventName;
    }

    public LogInfo setEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public LogInfo setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public LogInfo setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public long getCostTime() {
        return costTime;
    }

    public LogInfo setCostTime(long costTime) {
        this.costTime = costTime;
        return this;
    }

    public Object[] getRequest() {
        return request;
    }

    public LogInfo setRequest(Object... request) {
        this.request = request;
        return this;
    }

    public Object getResponse() {
        return response;
    }

    public LogInfo setResponse(Object response) {
        this.response = response;
        return this;
    }

    public Object getOthers() {
        return others;
    }

    public LogInfo setOthers(Object others) {
        this.others = others;
        return this;
    }
}