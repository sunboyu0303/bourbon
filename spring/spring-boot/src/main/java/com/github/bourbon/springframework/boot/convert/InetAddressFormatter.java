package com.github.bourbon.springframework.boot.convert;

import org.springframework.format.Formatter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 14:08
 */
final class InetAddressFormatter implements Formatter<InetAddress> {

    @Override
    public String print(InetAddress object, Locale locale) {
        return object.getHostAddress();
    }

    @Override
    public InetAddress parse(String text, Locale locale) throws ParseException {
        try {
            return InetAddress.getByName(text);
        } catch (UnknownHostException ex) {
            throw new IllegalStateException("Unknown host " + text, ex);
        }
    }
}