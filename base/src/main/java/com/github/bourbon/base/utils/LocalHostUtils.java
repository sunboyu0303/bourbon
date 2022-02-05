package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.CharConstants;

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
    private static String ip16;
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
            StringBuilder sb = new StringBuilder();
            for (String column : ip.split("\\.")) {
                String hex = HexUtils.toHex(Integer.parseInt(column));
                if (hex.length() == 1) {
                    sb.append(CharConstants.ZERO).append(hex);
                } else {
                    sb.append(hex);
                }
            }
            ip16 = sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String ip() {
        return ip;
    }

    public static String ip16() {
        return ip16;
    }

    public static String hostName() {
        return hostName;
    }

    private LocalHostUtils() {
    }
}