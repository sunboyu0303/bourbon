package com.github.bourbon.base.lang.cluster;

import com.github.bourbon.base.appender.builder.JsonStringBuilder;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/3 17:24
 */
public class ServerDescriptor {

    private final String host;
    private final int port;
    private String type;

    public ServerDescriptor(String host, int port) {
        this(host, port, "default");
    }

    public ServerDescriptor(String host, int port, String type) {
        this.host = host;
        this.port = port;
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getType() {
        return type;
    }

    public ServerDescriptor setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return new JsonStringBuilder(true)
                .appendBegin()
                .append("host", host)
                .append("port", port)
                .append("type", type)
                .appendEnd()
                .toString();
    }
}