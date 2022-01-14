package com.github.bourbon.base.system;

import com.github.bourbon.base.utils.SystemUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 14:32
 */
public class JavaInfo {
    private final String version = SystemUtils.get("java.version");
    private final String vendor = SystemUtils.get("java.vendor");
    private final String vendorUrl = SystemUtils.get("java.vendor.url");

    private final boolean isJava1 = javaVersionMatches("1.1");
    private final boolean isJava2 = javaVersionMatches("1.2");
    private final boolean isJava3 = javaVersionMatches("1.3");
    private final boolean isJava4 = javaVersionMatches("1.4");
    private final boolean isJava5 = javaVersionMatches("1.5");
    private final boolean isJava6 = javaVersionMatches("1.6");
    private final boolean isJava7 = javaVersionMatches("1.7");
    private final boolean isJava8 = javaVersionMatches("1.8");
    private final boolean isJava9 = javaVersionMatches("9");
    private final boolean isJava10 = javaVersionMatches("10");
    private final boolean isJava11 = javaVersionMatches("11");
    private final boolean isJava12 = javaVersionMatches("12");
    private final boolean isJava13 = javaVersionMatches("13");
    private final boolean isJava14 = javaVersionMatches("14");
    private final boolean isJava15 = javaVersionMatches("15");
    private final boolean isJava16 = javaVersionMatches("16");

    JavaInfo() {
    }

    public final String getVersion() {
        return version;
    }

    public final String getVendor() {
        return vendor;
    }

    public final String getVendorURL() {
        return vendorUrl;
    }

    public final boolean isJava1() {
        return isJava1;
    }

    public final boolean isJava2() {
        return isJava2;
    }

    public final boolean isJava3() {
        return isJava3;
    }

    public final boolean isJava4() {
        return isJava4;
    }

    public final boolean isJava5() {
        return isJava5;
    }

    public final boolean isJava6() {
        return isJava6;
    }

    public final boolean isJava7() {
        return isJava7;
    }

    public final boolean isJava8() {
        return isJava8;
    }

    public final boolean isJava9() {
        return isJava9;
    }

    public final boolean isJava10() {
        return isJava10;
    }

    public final boolean isJava11() {
        return isJava11;
    }

    public final boolean isJava12() {
        return isJava12;
    }

    public final boolean isJava13() {
        return isJava13;
    }

    public final boolean isJava14() {
        return isJava14;
    }

    public final boolean isJava15() {
        return isJava15;
    }

    public final boolean isJava16() {
        return isJava16;
    }

    private boolean javaVersionMatches(String prefix) {
        return version != null && version.startsWith(prefix);
    }
}