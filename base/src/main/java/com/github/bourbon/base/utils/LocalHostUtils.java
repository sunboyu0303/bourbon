package com.github.bourbon.base.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/2 16:01
 */
public final class LocalHostUtils {

    private static String ip;
    private static String hostName;

    static {
        try {
            InetAddress address = InetAddress.getLocalHost();
            hostName = address.getHostName();
            ip = address.getHostAddress();
            if (address.isLoopbackAddress()) {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    Enumeration<InetAddress> enumeration = interfaces.nextElement().getInetAddresses();
                    while (enumeration.hasMoreElements()) {
                        InetAddress nextElement = enumeration.nextElement();
                        if (!nextElement.isLoopbackAddress() && nextElement instanceof Inet4Address) {
                            ip = nextElement.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String ip() {
        return ip;
    }

    public static String hostName() {
        return hostName;
    }

    private LocalHostUtils() {
    }
}